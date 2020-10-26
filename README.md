## Cats-n-Dogs
A simple app for displaying regularly updating photos of cats and dogs. The users local sunrise and
sunset time will determine whether cats or dogs are currently visible.##

## Dependencies
- Android Studio 4.0+

## Architecture
- Single Activity app
- Fragments act as the view in MVVM
- Using Android ViewModels with coroutines

## Future improvements
- Add a Repository layer to handle any network/db interactions
and move retrofit calls currently in the ViewModel to the repositories. This
would allow our ViewModels to be cleaner, with a clearer separation of concerns.
- Add a Room database to support offline mode. Here we could save some small number of
cat and dog photos to cycle through when the user is offline.
- Add more robust error handling with fallbacks for when network requests. Currently we're
just defaulting the user to a location of NYC and have no fallbacks if pet or solar data fails.
- Add more robust testing 