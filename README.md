# Gravity
[![Build Status](https://travis-ci.com/reugn/gravity.svg?branch=master)](https://travis-ci.com/reugn/gravity)
[ ![Download](https://api.bintray.com/packages/reug/maven/gravity/images/download.svg) ](https://bintray.com/reug/maven/gravity/_latestVersion)

`Gravity` is a Java string matching library with a rich multi-pattern and simple string match interfaces.
It provides an ability to match against InputStream without loading the whole target into the memory.

`Gravity` wraps [Apache Tika](https://github.com/apache/tika) to detect and extract metadata and structured text content from various document formats.

## Getting Started
Add Maven/Gradle repository:
```
https://dl.bintray.com/reug/maven
```
Gradle dependency:
```
implementation 'com.github.reugn:gravity:<version>'
```

## Installation
* Install [TikaOCR](https://cwiki.apache.org/confluence/display/TIKA/TikaOCR) to search text inside an image.

## Usage examples
Single pattern matcher:
```java
InputStream is = Utils.readResource("/apache-license-2.0.txt");
Pattern pattern = new Pattern("within third-party archives", 1);

Matcher m = new BMHMatcher();
int res = m.match(pattern, is, 2);
assertEquals(1, res)
```

Use ad hoc matcher for multiple patterns:
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

Load patterns from a CSV file and use Trie matcher:
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
