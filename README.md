# Sparser



## Supported Predicates

| Filters         | Description                                                                                                                                | Examples                                      |
|-----------------|--------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------|
| Exact Match     | Searches record for the exact match of the given substring.                                                                                | WHERE user.name = "Foo" <br/> WHERE count = 5 |
| Key-Value Match | Searches record for the exact match of the given substring. Unlike `Exact Match` filter, it <br/>makes sure that value belongs to the key. | WHERE user.authorized = true                  |

### Limitations

Limitations on predicate support:

* Doesn't support equality for data types which can be encoded in different ways. For example, in JSON integer 
  equality is not supported if an integer can be both "3.4" and "34e-1".
* Doesn't support inequality for string values(???).
* `Key-Value Match` filter is only valid for data formats such as JSON where keys explicitly exist in the record.

