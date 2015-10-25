# Gradle Format plugin #

The Gradle Format plugin enables you to format your Java sources. It operates in place and can be used for instance
before pushing changes to enforce a coding style. The formatting can be customized using a properties or XML file with
the keys defined in [DefaultCodeFormatterConstants](http://help.eclipse.org/indigo/index.jsp?topic=%2Forg.eclipse.jdt.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fjdt%2Fcore%2Fformatter%2FDefaultCodeFormatterConstants.html)
 from Eclipse. See src/test/resources/ for examples.
 Eventually you should be able to export Eclipse settings to this file.

The plugin also enables you to sort imports.

## Usage

To use the Format plugin, include in your build script:

    apply plugin: 'com.github.youribonnaffe.gradle.format'

Note that the Java plugin will be applied too.

To run it:

    gradle format

The plugin JAR needs to be accessible in the classpath of your build script. It is directly available on
[Gradle plugins](https://plugins.gradle.org/).
Alternatively, you can download it from GitHub and deploy it to your local repository. The following code snippet shows an
example on how to get it and use it:

    buildscript {
      repositories {
        maven {
          url "https://plugins.gradle.org/m2/"
        }
      }
      dependencies {
        classpath "com.github.youribonnaffe.gradle.format:gradle-format-plugin:1.3"
      }
    }
    
    apply plugin: "com.github.youribonnaffe.gradle.format"

## Tasks

The Format plugin defines the following tasks:

* `format`: Format Java source code

## Properties

The Format task defines the following properties:

* `configurationFile`: The formatter configuration (File)
* `importsOrder`: The import orders (List of strings)
* `importsOrderConfigurationFile`: The import orders (a file using Eclipse format)
* `files`: The files to format (FileCollection), defaults are Java sources from main and test SourceSets

### Example

    format {
        configurationFile = file('myformatsettings.properties')
        importsOrder = ["java", "javax", "org", "\\#com.something.StaticImport"]
        files = sourceSets.main.java
    }
## Acknowledgements

Using [Hibernate Tools](http://hibernate.org/tools/) JavaFormatter

Import ordering from [EclipseCodeFormatter](https://github.com/krasa/EclipseCodeFormatter)

Readme format from https://github.com/bmuschko/gradle-gae-plugin
