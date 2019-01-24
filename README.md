## Gravity
[ ![Download](https://api.bintray.com/packages/reug/maven/gravity/images/download.svg) ](https://bintray.com/reug/maven/gravity/_latestVersion)

Gravity is a Java data matching library with rich multi-pattern and simple string match interfaces.
Provides ability to match against InputStream without loading the whole target into memory.

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
### Usage examples
Single pattern matcher
```java
InputStream is = Utils.readResource("/apache-license-2.0.txt");
Pattern pattern = new Pattern("within third-party archives", 1);

Matcher m = new BMHMatcher();
int res = m.match(pattern, is, 2);
assertEquals(1, res)
```
Use ad hoc matcher for multiple patterns
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
Load patterns from CSV file and use Trie matcher
```java
InputStream patterns_is = Utils.readResource("/patterns.csv");
List<Pattern> patterns = Patterns.fromCSV(patterns_is, Filters::specialChars);
SpecifiedMatcher sm = new TrieMatcher(patterns, 3);
InputStream is = Utils.readResource("/apache-license-2.0.txt");
int res = sm.match(is).get().get();
assertEquals(18, res);
```