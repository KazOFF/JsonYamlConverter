package ru.kazov.jyco;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import org.apache.commons.cli.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

public class Main {
  private static final Set<String> ACCEPTED_TYPES = Set.of("json", "yaml", "yml");
  private static final String SYNTAX = "jyco.exe";
  private static Options options;

  public static void main(String[] args) {
    setupCliOptions();
    CommandLine cmd;
    try {
      cmd = new DefaultParser().parse(options, args);
    } catch (ParseException e) {
      if (e instanceof MissingOptionException) {
        System.err.println("Missing required options");
        printHelp();
      } else if (e instanceof MissingArgumentException) {
        System.err.println("Missing arguments");
        printHelp();
      } else {
        System.err.println("Can't parse command line arguments");
      }
      return;
    }

    if (cmd.hasOption("h")) {
      printHelp();
      return;
    }

    String template = cmd.getOptionValue("t");
    if (!new File("./templates/" + template).exists()) {
      System.err.println("Template not exists");
      return;
    }

    File inFile = new File(cmd.getOptionValue("i"));
    if (!inFile.exists()) {
      System.err.println("Input file not exists");
      return;
    }
    String inFilename = inFile.toPath().getFileName().toString();

    String type = getExtension(inFile.getName());
    if (!ACCEPTED_TYPES.contains(type)) {
      System.err.println("file type must be json or yaml");
      return;
    }

    String outFilename = "";
    if (cmd.hasOption("o")) {
      outFilename = cmd.getOptionValue("o");
    }

    Map<String, Object> map = null;
    try {
      Object obj = "json".equals(type) ? loadJson(inFile) : loadYaml(inFile);
      if (obj == null) {
        System.err.println("Transforming object from file is empty");
        return;
      }
      map =
          Map.of(
              "$root", obj,
              "$template", template,
              "$in", inFilename,
              "$out", outFilename,
              "$type", type,
              "$date", new Date());
    } catch (IOException e) {
      System.err.println("Can't read input file");
      return;
    }

    String out;
    try {
      out = generate(map, template);
    } catch (TemplateException | IOException e) {
      System.err.println("Can't generate file from template");
      if (cmd.hasOption("d")) {
        System.err.println(e.getMessage());
      }
      return;
    }

    if (cmd.hasOption("o")) {
      try (FileWriter fw = new FileWriter(cmd.getOptionValue("o"), StandardCharsets.UTF_8)) {
        fw.write(out);
        fw.flush();
      } catch (IOException e) {
        System.err.println("Can't save out file");
        return;
      }
      return;
    }
    System.out.println(out);
  }

  private static String generate(Map<String, Object> map, String template)
      throws IOException, TemplateException {
    Configuration cfg = new Configuration(Configuration.VERSION_2_3_33);
    cfg.setDirectoryForTemplateLoading(new File("./templates"));
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    cfg.setLogTemplateExceptions(false);
    cfg.setWrapUncheckedExceptions(true);
    cfg.setFallbackOnNullLoopVariable(false);

    Template temp = cfg.getTemplate(template + "/index.ftl");
    try (StringWriter sw = new StringWriter()) {
      temp.process(map, sw);
      sw.flush();
      return sw.toString();
    }
  }

  private static void setupCliOptions() {
    options = new Options();
    options.addOption("h", "help", false, "Show this help");
    options.addOption("d", "debug", false, "Debug template errors");
    options.addRequiredOption("i", "input", true, "Input file (json or yaml)");
    options.addOption("o", "output", true, "Output file");
    options.addRequiredOption("t", "template", true, "Template name");
  }

  private static Object loadJson(File file) throws IOException {
    String jsonStr = Files.readString(file.toPath(), StandardCharsets.UTF_8);
    try {
      return new JSONObject(jsonStr).toMap();
    } catch (Throwable ignored) {
    }
    return new JSONArray(jsonStr).toList();
  }

  private static Object loadYaml(File file) throws IOException {
    Yaml yaml = new Yaml();
    try (FileInputStream fis = new FileInputStream(file)) {
      return yaml.load(fis);
    }
  }

  private static String getExtension(String filename) {
    return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
  }

  public static void printHelp() {
    HelpFormatter formatter = HelpFormatter.builder().get();
    formatter.printHelp(SYNTAX, options);
  }
}
