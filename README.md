

# Introduction


# Technologies

### Backend
Revendiquons' data such as user information, proposition information or user votes are stored on a remote **MySQL** database. We interact with the database using **PHP**. Our web server is hosted on **Digital Ocean**  (a cloud hosting provider).

### API Calls
API calls are performed using the **Volley** library as recommanded by Google for HTTP requests. It does the same job as HttpUrlConnection with the benefit (among many) of handling the background execution on the developer behalf. This feature is very convenient.

### Persistance
Once data retrieved from the server, they are stored on a local database on the mobile itself. We use a **Room Database** which is now Google standard for persisting data locally. Room brings an abstraction layer over SQLite, allowing to interact with the database in more seamless way. The goal of persisting data locally is to allow the user to use the mobile application even when he does not have network access.

### Architectural Patterns
This application uses two major architectural patterns to shape the application design: the **repository pattern** and Google's **MVVM** pattern which stands for **Model-View-ViewModel**.

- The Repository Pattern's purpose is to provide a clear a seamless interface to the database. It's an abstraction layer over the database acting as the *single source of truth*.
- The MVVM pattern has greatly shaped the design of our application. In fact, it is the core concept around which the other components of the application are revolving. It brings several key features. </br>First, The data related to an activity are stored on its assigned ViewModel class. Therefore, when the activity is destroyed, all the data are still right at hand when the activity is restarted. This way, data are displayed quickly, therefore improving the user experience.
</br>Second, MVVM uses **LiveData** objects to observe data changes in Room database. Those Livedata objects are themselve observed by the UI. Therefore, each time there is a change in the database, the UI is instantly refreshed to display what's in the database. Lifecycle issues are handled by the LiveData library and we do not have to manually hydrate the UI.

### Expandable Recycler View
We wanted to display propositions in a way that would allow the user to quickly understand what it deals about. In the mean time we wanted it to be possible to learn more about a given proposition by staying on the same activity. So we decided that a list displaying proposition titles with a description unfolding underneath at user click would be the perfect UI component.</br>
**Recycler View** (which is the new Google standard for list rendering over old Listview) did not support expandables items. Hopefully, we found a quite popular library on Github called **Expandable-Recycler-View**. https://github.com/thoughtbot/expandable-recycler-view</br>It is bases on Google's recycler view and add this expandable on click functionality.

# Upcoming Features
An application's development is never complete. There are always things to improve or functionalities to add. Revendiquons is only a newborn, and the potential upgrades it needs to become a real application are almost infinite. Nevertheless, the nextcoming features we want to add to this app are:
- A toolbar with a logout button
- When the user is logged in the application, we he relaunch it, he is automatically logged in and redirected to the main page.
- A refresh function: when the user scroll's up the list, data are fetched from server.
- Animations: when going from one activity to the other, loading snippet when fetching data...
- Filters to select what to display in the list.