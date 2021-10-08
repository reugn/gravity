# Gravity
[![Build](https://github.com/reugn/gravity/actions/workflows/build.yml/badge.svg)](https://github.com/reugn/gravity/actions/workflows/build.yml)

`Gravity` is a Java string matching library with a rich multi-pattern and simple string match interfaces.
It provides an ability to match against an InputStream without loading the whole target into the memory.

`Gravity` wraps [Apache Tika](https://github.com/apache/tika) to detect and extract metadata and structured text content from various document formats.

## Getting Started
### Build from source
```sh
./gradlew clean build
```

### Install as a dependency
Read on [how to install](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry#installing-a-package) the `gravity` package from GitHub Packages.

## Installation
* Install [TikaOCR](https://cwiki.apache.org/confluence/display/TIKA/TikaOCR) to search text inside an image.

## Usage examples
A single pattern matcher:
```java
InputStream is = Utils.readResource("/apache-license-2.0.txt");
Pattern pattern = new Pattern("within third-party archives", 1);

Matcher m = new BMHMatcher();
int res = m.match(pattern, is, 2);
assertEquals(1, res)
```

Use an ad hoc matcher for multiple patterns:
```java
InputStream is = Utils.readResource("/apache-license-2.0.txt");
List<Pattern> patterns = new ArrayList<>();
patterns.add(new Pattern("within third-party archives", 10, 5));
patterns.add(new Pattern("modified files", 5, 5));
patterns.add(new Pattern("class name", 2, 5));

MultiMatcher m = new ConcurrentMultiMatcher(ContainsMatcher::new, 32);
int res = m.match(patterns, is).get().get();
assertEquals(17, res);
```

Load patterns from a CSV file and use the Trie matcher:
```java
InputStream patterns_is = Utils.readResource("/patterns.csv");
List<Pattern> patterns = Patterns.fromCSV(patterns_is, Filters::specialChars);
SpecifiedMatcher sm = new TrieMatcher(patterns, 3);
InputStream is = Utils.readResource("/apache-license-2.0.txt");
int res = sm.match(is).get().get();
assertEquals(18, res);
```

## License
Licensed under the Apache 2.0 License.
