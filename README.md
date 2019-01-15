## Gravity
[ ![Download](https://api.bintray.com/packages/reug/maven/gravity/images/download.svg) ](https://bintray.com/reug/maven/gravity/_latestVersion)

Gravity is a Java data matching library which provides reach multi-pattern search interface as well as simple string match.
It can iterate over InputStream without loading the whole target into memory.

Wraps [Apache Tika](https://github.com/apache/tika) for detecting and extracting metadata and structured text content from various document formats.

### Getting Started

Add  Maven/Gradle repository
```
https://dl.bintray.com/reug/maven
```
Gradle dependency
```
compile(group: 'reug', name: 'gravity', version: '<version>')
```
Usage example
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