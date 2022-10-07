# Sparser

<!-- TOC -->
* [Sparser](#sparser)
  * [Filter Primitives](#filter-primitives)
    * [Supported Filter Primitives](#supported-filter-primitives)
      * [Exact Match Filter](#exact-match-filter)
      * [Key-Value Match Filter](#key-value-match-filter)
  * [Limitations](#limitations)
  * [How to Use?](#how-to-use)
  * [Benchmarks](#benchmarks)
    * [More on Performance](#more-on-performance)
  * [Learnings](#learnings)
  * [To-Do](#to-do)
<!-- TOC -->

Applications that run on unstructured or semi-structured data spend considerable amount of their execution time 
parsing the data. Sparser strives to address this bottleneck by introducing the concept of _filtering data before 
parsing_.

There are two key observations that reinforce the idea of filtering:

- High selectivity: in Sparser's paper, authors show that queries most of the time have high selectivity. Not having 
  to consider a large portion of data that you're querying can truly bring some performance gain.
- Modern hardware: vectorized instructions of modern hardware can be utilized to make filtering/parsing faster. This 
  observation is irrelevant to environments like JMV where (at least currently) you don't have low-level control of 
  hardware.

## Filter Primitives

Filtering primitives are filter operators that Sparser uses to decide whether to filter data or not.

### Supported Filter Primitives

#### Exact Match Filter

Being the main filtering primitive of Sparser, Exact Match Filter searches for the exact match of a string in the given 
record.

Let's take the predicate `event = "RepositoryCreate" AND plang = "rust" AND lang = "en"` as an example. Exact Match 
Filter could search for a variety of substrings to determine whether a record can be filtered or not. If we assume the data is in 
UTF-8, here are some of the options: `Repo`, `toryCreate`, `ust`, `en`. 

To prove that **Sparser produces false positives, but not false negatives**, hypothetically, let's say Sparser has chosen 
the substring `en` to decide to filter or not. In this case `en` always matches as it is a substring of the key 
`event`. 
However, this is not the `en` that we are looking for, a false positive. If any of those substring is absent in the 
record, we can safely discard the record as its absence is certain.

#### Key-Value Match Filter

> Note: This filter is currently not supported.

Besides searching for the exact match of a string in the given record, Key-Value Match Filter makes sure that the value 
being looked up belongs to the key.

### Challenges

There are two key challenges in designing filtering primitives:
1. Filters should be hardware-efficient. Certain families of string search algorithms perform better under certain 
   conditions. Therefore, size of substring or any other factor that affects search time should be taken into 
   account while compiling substrings. For example, the SIMD-based algorithm that authors suggested is optimized for 
   2-, 4-, and 8-byte substring look-ups.
2. Choosing an efficient filter cascade. To decrease the overall false positive rate, a set of filtering primitives 
   are combined into a cascade. In this case, all substrings inside cascade are looked-up for existence before being 
   able to discard a record. The problem is two-fold: (1) Sparser should choose the most optimal cascade and (2) 
   since the search space for choosing the best cascade is combinatorial, Sparser should do it fast.

Sparser's solutions for the challenges above:
1. asfda
2. parser encompasses a cost-based [optimizer](./core/src/main/java/com/meylism/sparser/core/optimization/CostBasedOptimizer.java)
   that help select the optimal cascade. Furthermore, Sparser [prunes](./core/src/main/java/com/meylism/sparser/core/optimization/transformation/FilterPruningTransformer.java) the search space by
   restricting the number of filter primitives considered to avoid long search times.

## Limitations

Limitations on predicate support:

* Doesn't support equality for data types which can be encoded in different ways. For example, in JSON integer 
  equality is not supported if an integer can be both "3.4" and "34e-1".
* Doesn't support inequality for string values(???).
* `Key-Value Match` filter is only valid for data formats such as JSON where keys explicitly exist in the record.

## How to Use?

## Benchmarks

### More on Performance

## Learnings

Link to the Sparser's paper: [Filter Before You Parse: Faster Analytics on Raw Data with Sparser](https://www.vldb.org/pvldb/vol11/p1576-palkar.pdf).

Link to the video presentation by Sparser's authors: [Faster Parsing of Unstructured Data Formats in Apache Spark ](https://youtu.be/Cpk9VvUSSUg)

Link to the partial implementation of Sparser by its authors: [Sparser on GitHub](https://github.com/stanford-futuredata/sparser)

## To-Do

TODO:

- [ ] implement isDNF
- [ ] fix benchmark thing
- [ ] how java string search algorithm works
