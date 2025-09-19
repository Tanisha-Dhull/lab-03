# CMPUT 301 : Lab 3 Participation Exercise

## References and Resources

**OpenAI ChatGPT 5.0**

Below is the attached prompt and response used for guidance:

Prompt :  
Assist me in figuring out how to add a functionality into a given implemented app using Java in AndroidStudio. We are provided a starter code, where the app adds a city along with its state in a list of cities being displayed. Now, we want to add another functionality which can edit the city's name and state when we click on it. Hints: 1. Add setters to your City class so that you can modify its name and province. You may need to pass the City object that is clicked in the Activity into the Fragment. One way to do this is to make a constructor for the Fragment that takes in a City, and store the City in the Fragment as an instance variable. If you do this, make sure to also add an empty constructor to the Fragment so you can use it when adding a new City. Another way you can send a City object from the Activity to the Fragment is by adding a "newInstance" method in the Fragment, and use this method to create a new Fragment when editing a City. In this method, you take in a City and store it in the Fragment's Bundle object (if you do this, make sure that your City class implements the "Serializable" interface, so that it can be saved into the Bundle). Later on in your onCreateDialog method, you can access the Bundle using getArguments() and retrieve the City object there. Note that storing data in a Bundle is similar to storing data in an Intent using key-value pairs. (This is the preferred way to do it in Android, because you usually should not have custom constructors for your Fragments. See here for more info).

Response:  
Perfect—here’s a copy-free, step-by-step plan to add “tap-to-edit” using your existing add-city dialog, with only a few new methods and small glue changes.

What you’ll add (overview)
City.java  
Make the class serializable.  
Add two setters.  
Add/Edit DialogFragment (reuse your add dialog or rename it)  
Add two argument keys.  
Add a newInstance(...) factory to pass data in.  
Add a tiny interface (callback) to send results back.  
In the dialog: detect Add vs Edit, prefill fields, and call the callback.  
MainActivity  
Implement the callback.  
Open the dialog in two ways: FAB = Add, List item tap = Edit.  
Update the list + notify the adapter.

1. City.java (2 tiny changes)
File: City.java
Goal: allow passing and editing.
Add implements Serializable to the class header.
Why: so you can put a City into a Fragment Bundle without errors.
Add two setters:
setCityName(String name)
setProvinceName(String province)
Why: so you can update an existing City when the user edits.
(Check) Your toString() should return something like "City, Province".
Why: ArrayAdapter uses toString() to show the row text.

2. Your DialogFragment: make it work for Add + Edit
You can keep your current AddCityFragment and just enhance it.  
File: AddCityFragment.java (or rename to AddEditCityFragment if you like)  
2.1 Add two argument keys (as static final String)  
ARG_CITY – to carry the city being edited (can be null for add).  
ARG_POSITION – the index in the list (use -1 for add).  
Keep the empty public constructor you already have. Don’t add custom constructors.  
2.2 Add a factory method  
Add a public static method:  
Name: newInstance(City cityOrNull, int positionOrMinusOne)  
Inside it:  
Create a Bundle  
Put the city with putSerializable(ARG_CITY, cityOrNull)  
Put the position with putInt(ARG_POSITION, positionOrMinusOne)  
Create the fragment, call setArguments(bundle), and return it.  
Why: This is the Android-preferred way (no custom constructors) and avoids config-change issues.  
2.3 Add a small callback interface  
Define inside the fragment:  
Interface name: OnCityEditedListener  
Single method:  
void onCitySaved(String name, String province, int position, boolean isEdit);  
Why: Pass plain strings + position back to the Activity. This avoids the “edited copy vs original object” trap when using Serializable.  
You will call this after the user presses Add/Save.  
2.4 In onCreateDialog(...):  
Read arguments:  
Get the City from getArguments().getSerializable(ARG_CITY) (may be null).  
Get position from getArguments().getInt(ARG_POSITION, -1).  
Make a boolean isEdit = (city != null).  
Inflate the same layout you use for adding (two EditTexts).  
If isEdit == true, prefill those EditTexts with the city’s current name/province.  
When the user presses the positive button:  
Read String newName and String newProvince.  
Call the listener:  
onCitySaved(newName, newProvince, position, isEdit)  
Keep validation simple (e.g., ensure neither field is empty). If invalid, just don’t call the listener.

3. MainActivity: wire it up  
File: MainActivity.java
3.1 Implement the fragment’s interface  
Add implements AddEditCityFragment.OnCityEditedListener (or your class name).  
Implement onCitySaved(name, province, position, isEdit) like this:  
If isEdit:  
Grab the original item: cityDataList.get(position)  
Call its two setters with name and province  
Call adapter.notifyDataSetChanged()  
Else (adding):  
Create a new City(name, province)  
cityDataList.add(...)  
adapter.notifyDataSetChanged()  
3.2 Open the dialog  
For Add (FAB click):  
Create the dialog with newInstance(null, -1)  
show() it.  
For Edit (ListView item click):  
Inside your setOnItemClickListener, you already have position.  
Get the clicked city from your list.  
Create the dialog with newInstance(clickedCity, position)  
show() it.  
Make sure your ListView and FAB IDs match what you call with findViewById.  
Name consistency matters—if your list is cityDataList everywhere, stick to that exact name.

4. Quick test plan (2 minutes)  
Launch the app: you should see your seeded cities.  
Tap a city → dialog title should say Edit City and fields prefilled.  
Change both fields → Save → row text updates.  
Tap FAB → dialog title should say Add City and fields empty.  
Add a new city → a new row appears.  
Rotate the device: try editing/adding again to ensure no crashes.  
Common pitfalls (and what fixes them)  
incompatible types: City cannot be converted to Serializable  
You forgot implements Serializable in City.java.  
“App stops responding” when tapping a city  
Usually either:  
The dialog newInstance(...) wasn’t used and you rely on a missing constructor, or  
Your layout IDs in the fragment don’t match the actual XML IDs, or  
The Activity didn’t implement the fragment’s interface (so the cast fails).  
Fix: implement the interface and ensure you call it.  
Edits don’t show up  
You updated a copy of City in the dialog.  
Fix: in the Activity’s onCitySaved(...), update the original list item via the setters and then notifyDataSetChanged().  
cannot find symbol citydataList  
Use the same field name everywhere. If your list is cityDataList, don’t call it citydataList in one file and cities in another.

That’s it. If you tell me your exact class/file names and the IDs in your XML (ListView, FAB, EditTexts), I’ll map each step to your filenames/IDs one-by-one to make it foolproof.


## Verbal Collaboration

| Student Name | CCID      |
| ------------ | --------- |
| `student`    | `student` |
| `<Add more>` | `<CCID>`  |
