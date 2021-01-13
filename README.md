# Decoupled Android Search

I was working on a project where we had to support a couple of features regarding searching different resources. Although the ways of searching and displaying the resources were very different, I was able to come up with a solution, based on a classic design pattern, to reuse some common UI features that had to be consistent between them.

In this article I'll show a very simple app that support searching dogs and animes, as shown below, in order to present my solution.

| Dog search                           | Anime Search                             |
|--------------------------------------|------------------------------------------|
| ![dog search](images/dog-search.gif) | ![anime search](images/anime-search.gif) |

First, the user is taken to a "filter screen" where is possible to enter the desired search parameters. 

When the user submits the desired search, a "search result screen" opens up, loading the results of the user query.

If he/she wants to make a new search, by tapping the `FloatingActionButton` he/she is taken back to the "filter screen" witch should display the previous submitted query.

There are a couple of behaviours that are equal on the "search results screen" in both features presented above, such as:

* retrieving the search parameters the user provided on the "filter screen";
* displaying the search results;
* the app bar changes according to the search parameters chosen by the user;
* there is a loading animation when fetching the next results page (a `SwipeRefreshLayout` on the images above);
* a "invalid filter" warning if the user choses a invalid search parameters combination, e.g. an empty search (not shown above);
* a `View` that redirects back to the "filter screen" (represented as the `FloatingActionButton` above) passing the current filter.

The design proposed here try to abstract and reuse the behaviours above between all search features of the app.

## Proposed solution

In order to reuse some code between the two search features (and future ones), guaranteeing the common UI behaviours described above in a manageable way, and applying---to some degree---the [open-closed principle](https://en.wikipedia.org/wiki/Open%E2%80%93closed_principle), I came with a solution using the [abstract factory design pattern](https://en.wikipedia.org/wiki/Abstract_factory_pattern).

The solution is based on having the "search results screen" as an `Activity` with the common UI features and behaviours such as the loading animation, app bar, and the floating action button. This `Activity` (we will call it the `SearchableActivity` from now) is used for every search result provided by the app, it will receive an instance of a abstract factory accordingly to the type of search requested, this factory can create:

* an empty `SearchFilter` for the given search type;
* an `Intent` that will open another `Activity` that represents the "search filter screen" of that type of search, this activity will receive the current `SearchFilter` so it can render it to the user;
* a `Fragment` that is responsible to search and render a provided `SearchFilter`.

The reason the factory can create an empty `SearchFilter` is in order to send it to the "search filter screen" on the first time, so it can know that the user is making a new search from scratch, and not editing an old one.

Those abstractions enable the `SearchableActivity` to:

* call (through an intent) the "search filter screen" and receive from it the search query as an abstract `SearchFilter` class;
* attach a `Fragment` (which we will call the `SearchableFragment` from now on) that can search and render the current `SearchFilter`.

Without any knowledge of what kind of search it is performing, or what kind of data this search requires.

It also enables the `SearchableFragment` to:

* change the app bar title;
* show a loading animation;
* notifying and invalid search filter to the user;

All of that without implementing those UI features, and delegating those actions to the `SearchableActivity` that it is attached on.

Starting the search feature for dogs is something like this:

```kotlin
val dogSearchIntent = Intent(context, SearchableActivityImpl::class.java)
	.putExtra(SearchActivity.EXTRA_KEY, DogSearchFactory)

startActivity(dogSearchIntent)
```

The following is the class diagram for the solution, some of the components had to implement the `Parcelable` interface in order to pass them between `Activity` instances.

[![diagram](images/decoupled-search-class-diagram.svg "Diagram")](https://mermaid-js.github.io/mermaid-live-editor/#/edit/eyJjb2RlIjoiY2xhc3NEaWFncmFtXG5cbiAgY2xhc3MgU2VhcmNoRmFjdG9yeSB7XG4gICAgPDxpbnRlcmZhY2U-PlxuICAgICtjcmVhdGVFbXB0eVNlYXJjaEZpbHRlcigpOiBTZWFyY2hGaWx0ZXJcbiAgICArY3JlYXRlU2VhcmNoRmlsdGVySW50ZW50KCk6IEludGVudFxuICAgICtjcmVhdGVTZWFyY2hhYmxlRnJhZ21lbnQoKTogU2VhcmNoYWJsZUZyYWdtZW50PFNlYXJjaEZpbHRlcj5cbiAgfVxuXG4gIGNsYXNzIFNlYXJjaGFibGVBY3Rpdml0eSB7XG4gICAgPDxpbnRlcmZhY2U-PlxuICAgICtzaG93TG9hZGluZ0FuaW1hdGlvbigpXG4gICAgK2hpZGVMb2FkaW5nQW5pbWF0aW9uKClcbiAgICArbm90aWZ5SW52YWxpZEZpbHRlcigpXG4gICAgK3NldEFwcEJhclRpdGxlKHRpdGxlOiBTdHJpbmcpXG4gIH1cblxuICBjbGFzcyBTZWFyY2hhYmxlRnJhZ21lbnR-RmlsdGVyOiBTZWFyY2hhYmxlRmlsdGVyfntcbiAgICA8PGFic3RyYWN0Pj5cbiAgICAjc2VhcmNoYWJsZUFjdGl2aXR5OiBTZWFyY2hhYmxlQWN0aXZpdHlcblxuICAgICtuZXdJbnN0YW5jZShmaWx0ZXI6IFNlYXJjaEZpbHRlcikgU2VhcmNoYWJsZUZyYWdtZW50fkZpbHRlcn5cbiAgICAjY3JlYXRlU2VhcmNoYWJsZUZyYWdtZW50KCkqIFNlYXJjaGFibGVGcmFnbWVudH5GaWx0ZXJ-XG4gICAgI2dldFNlYXJjaEZpbHRlcigpIEZpbHRlclxuICB9XG5cbiAgY2xhc3MgU2VhcmNoRmlsdGVyIHtcbiAgICA8PGFic3RyYWN0Pj5cbiAgICArZ2V0RmlsdGVyRnJvbShidW5kbGUpJCBTZWFyY2hGaWx0ZXJcbiAgICAraXNFbXB0eSgpKiBib29sZWFuXG4gICAgK3RvQnVuZGxlKCkqIEJ1bmRsZVxuICB9XG4gIFxuICBjbGFzcyBQYXJjZWxhYmxlIHtcbiAgICA8PGludGVyZmFjZT4-XG4gIH1cblxuICBQYXJjZWxhYmxlIDx8Li4gU2VhcmNoRmFjdG9yeVxuICBQYXJjZWxhYmxlIDx8Li4gU2VhcmNoRmlsdGVyXG4gIFNlYXJjaEZhY3RvcnkgLS0-IFNlYXJjaGFibGVGcmFnbWVudFxuICBTZWFyY2hGYWN0b3J5IC0tPiBTZWFyY2hGaWx0ZXJcbiAgXG4gIFNlYXJjaGFibGVGcmFnbWVudCAtLT4gU2VhcmNoYWJsZUFjdGl2aXR5XG5cdFx0XHRcdFx0IiwibWVybWFpZCI6eyJ0aGVtZSI6ImRlZmF1bHQifSwidXBkYXRlRWRpdG9yIjpmYWxzZX0)

This enable us to have a single `SearchableActivity` implementation having those common UI features, and managing the search flow, and for every new search feature the app requires, we have to only implement the respective `SearchableFragment` that is responsible only for the specifics of it's search feature, a new `SearchFilter` representing the user search parameters choice, an `Activity` representing the "search filter screen" of that search feature, and a very simple `SearchFactory` class that puts all those things together.

That can be done without changing a single line of the `SearchableActivity`, and the programmer only have to implement what he cares about in that moment, that is, the search functionality.

## Was it really worth it?

Depending on who you ask, this is a complicated design, I don't think is really difficult to understand and maintain, but I won't enter in this discussion here.

What I can say is that it serve at least one purpose, before the app was released, I was asked to convert the `FloatingActionButton` that redirects to the "search filter screen", into a static button between the app bar and the result list (don't ask me) in all the search feature screens, and I only had to change the `SearchableActivity` layout.

Other than that, It was pretty fun design to think and implement, so it was worth for me.

Feel free to search the code base for the demo app, the interfaces and abstract classes are [here](app/src/main/java/com/example/decoupled_android_search/features/search/contract) and the search features implementations [here](app/src/main/java/com/example/decoupled_android_search/features/search/impl).

