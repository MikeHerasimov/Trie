# Trie

### Introduction

If you're not familiar with trie data-structure I suggest you to read [this] article first.
And [this one] about DAWG (directed acyclic word graph).

This project contains two trie data-structure implementations:
* ArrayTrie
* LinkedTrie

Also there are respective DAWGs, to which tries can be optimized:
* ArrayDAWG
* LinkedDAWG

### ArrayTrie

ArrayTrie is standard trie, called like this because of nodes structure, where each node contains reference to array of descendants. 
Size of such array always equals to lenght of alphabet regardless of current number of descendants. 
Children always holds in positions equal to their letter's index in alphabet.

The node-structure is next:
```sh
ArrayNode{
  boolean EOW;
  char letter;
  ArrayNode children[];
}
```

ArrayTrie is fastest Trie implementation.

### LinkedTrie

Nodes of LinkedTrie implement *doubly chained trie structure*, so instead of containing reference to array 
each node has references to first child and brother-node.

The node-structure is next:
```sh
LinkedNode{
  boolean EOW;
  char letter;
  LinkedNode child;
  LinkedNode brother;
}
```
LinkedTrie uses less memory then ArrayTrie, but it adds some overhead to its add() and contains() methods.

For example if you need to find some node among node's descendants you'll need continiously pass elements and check them for equality.
So you'll need to pass 1 element in best case and n elements in worst case (where n is number of children, don't confuse it with alphabet length). 

Difficulty of this algorithm is O(n/2). And this operation is widelly used in contains() and add() methods.
The same operation in ArrayTrie has constant complexity (O(1)), because we only need to check particular position in array.

Note that LinkedTrie can't make full optimization to DAWG that is it can't eliminate all duplicate nodes иecause of its node structure, 
where each node contains reference to its brother and child. When checking nodes for equality LinkedTrie has recursively check 
not only all descendants of node, it has to check brother-nodes too.

### ArrayDAWG & LinkedDAWG

ArrayDAWG and LinkedDAWG are both immutable. They can perfom only contains(), and size() methods.
Also they both has inner Builder class, which can be used to gracefully create ArrayDAWG or LinkedDAWG by supplied words. 
Number of nodes in ArrayDAWG is equal or smaller than number of nodes in LinkedDAWG.

### Version

Version 2.2.0

Were introduced changes in project, such as:
* interface Alphabet was added
* was added enum CommonAlphabets that holds instances for english, ukrainian and russian Alphabet`s
* method isPrefix() was added to interface WordGraph

Version 2.1.0

Were introduced changes in project, such as:
* all classes relevant to ArrayTrie were placed in *array* package and the same was did for classes, which are relevant to LinkedTrie, so they migrated to *linked* package
* Externalizable was implemented for ArrayTire and LinkedTrie classes

Version 2.0.0

Were introduced changes in project, such as:
* tagged class Trie from version 1.0.0 was splitted to hierarchy of classes
* ArrayTrie realization with respective ArrayDAWG was added

### Instalation
##### Maven projects
To use Trie in your project, build and install Trie artifact to local repository with Maven ```mvn clean install``` and add dependency in your project:
```
<dependency>
  <groupId>com.github.mikeherasimov</groupId>
  <artifactId>trie</artifactId>
  <version>0.2</version>
</dependency>
```
##### Non Maven projects
Build Trie with Maven 3 ```mvn clean package``` and attach ```target/trie-0.2.jar``` lib to your project 

Have fun!

### License

Standart MIT license

### Dedication

Dedicated to Maria Savschenko. You were a good pal, I miss you.

### Thanks

Thanks to Xaver Kapeller for supporting me all this time, pointing to my mistakes, giving advices and helping with project's architecture.
You are amazing man and developer ;)

[this]: <http://www.toptal.com/java/the-trie-a-neglected-data-structure>
[this one]: <https://en.wikipedia.org/wiki/Deterministic_acyclic_finite_state_automaton>
