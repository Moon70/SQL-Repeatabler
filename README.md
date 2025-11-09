# SQL-Repeatabler

When working with lo code platform [Appian](https://appian.com/), creating and modifying record types (SQL database tables) generates small SQL scripts containing i.e. 'create table' or 'modify table' statements, needed to deploy the database changes to the next zone (i.e. dev/test/prod).

Depending on the deployment tool used, the scripts may be executed more than once.
A 'create table' statement will fail when running the second time, because the table already exists.

This is where **Sql-Repeatabler** comes into play.

In case of a 'create table ...' statement, this tool puts a 'if not exists table...' in front of the 'create' statement.

<u>Limitations:</u>

* This is not a full featured SQL tool, but only covers simple scripts produced by the Appian platform when working with record types. It only will be enhanced as needed.
* This tool expects and generates SQL scripts in Microsoft flavour (T-SQL). It contains only very few corrections in case the source script is MySQL flavoured.

<u>Usage:</u>

* After start, the tool shows an few an ape related daily changing AI generated image. This is part of the core functionality, because the ape is our team mascot ;-).
* Drag´n drop one or more .sql files on the image.
* The tool opens a tab for each file, showing the original script on the left, the converted script on the right side.
* Manual check the generated script
* Use file menu 'Save As' to save all generated scripts to one file.



<u>Examples:</u>

![Example_CreateTable](B:\GitHub\SQL-Repeatabler\SQL-Repeatabler\readme\Example_CreateTable.jpg)

![Example_AlterTable](B:\GitHub\SQL-Repeatabler\SQL-Repeatabler\readme\Example_AlterTable.jpg)

![Example_SpRename](B:\GitHub\SQL-Repeatabler\SQL-Repeatabler\readme\Example_SpRename.jpg)