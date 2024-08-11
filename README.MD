# Json and Yaml COnverter
**jyco** is small utility, that helps  you to generate files from json or yaml data with templates.

**jyco** works with [Apache Freemarker](https://freemarker.apache.org/). Read [docs](https://freemarker.apache.org/docs/index.html) if you want to know how to create templates

## Usage
```jyco -i ./examples/test.json -o test.html -t test-html```

Run jyco without parameters to see all available options.

## Create template

All templates should be placed as a subfolder in the ```templates``` folder. The main template file should be named ```index.ftl```

## Data model
In template you can access to base objects.

| Object    | Desciption                                     |
|-----------|------------------------------------------------|
| $root     | Root object from json or yaml. Object or Array |
| $template | Template name, that used                       |
| $in       | Input filename                                 |
| $type     | Type of input file (json or yaml)              |
| $date     | Date, when conversion is started               |

## Author
Created for fun by Roman Kazov.