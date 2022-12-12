# Sparser

[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=meylism_sparser-port&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=meylism_sparser-port)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=meylism_sparser-port&metric=coverage)](https://sonarcloud.io/summary/new_code?id=meylism_sparser-port)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=meylism_sparser-port&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=meylism_sparser-port)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=meylism_sparser-port&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=meylism_sparser-port)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=meylism_sparser-port&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=meylism_sparser-port)

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=meylism_sparser-port&metric=bugs)](https://sonarcloud.io/summary/new_code?id=meylism_sparser-port)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=meylism_sparser-port&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=meylism_sparser-port)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=meylism_sparser-port&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=meylism_sparser-port)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=meylism_sparser-port&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=meylism_sparser-port)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=meylism_sparser-port&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=meylism_sparser-port)

<!-- TOC -->
* [Sparser](#sparser)
  * [Project Structure](#project-structure)
  * [Learnings](#learnings)
  * [To-Do](#to-do)
<!-- TOC -->

Exploratory big data applications that run un- or semi-structured data spend considerable amount of their execution time parsing the data. 
Academia has shown that most queries have [low selectivity](https://www.ibm.com/docs/en/informix-servers/14.10?topic=functions-query-selectivity).
However, parsing is expensive and therefore CPU cycles are wasted on parsing unnecessary data. Sparser strives to address this issue/bottleneck by 
introducing the concept of _filtering data before parsing_. As a result _only_ records that satisfy the predicate in query 
will be parsed by a parser.

Given a query predicate, Sparser, a system that filters "unnecessary" records, either builds an efficient filtering 
strategy by using its optimizer or "turns off" filtering at all if it deems filtering is of no help.

Therefore, Sparser is not a replacement but _a complement to existing parsers_.

Refer to the wiki of the project for more insights.

## Project Structure

The multimodule project includes the followings:

- benchmark - a separate utility project to facilitate benchmarking Sparser
- core - Sparser's core functionalities
- jacoco-report - contains no code and solely exist for the purpose of aggregating test coverage metrics from multiple modules 

## Learnings

Link to the Sparser's paper: [Filter Before You Parse: Faster Analytics on Raw Data with Sparser](https://www.vldb.org/pvldb/vol11/p1576-palkar.pdf).

Link to the video presentation by Sparser's paper's authors: [Faster Parsing of Unstructured Data Formats in Apache Spark ](https://youtu.be/Cpk9VvUSSUg).

## To-Do

TODO:

- [ ] implement isDNF
- [ ] change the implementation of subssearch
- [ ] re-optimization strategy
- [ ] raw filtering