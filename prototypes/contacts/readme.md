# SCM :: Simple Contact Manager

A small prototype to store email addresses. It uses the spring Framework and
Thymeleaf Templating Engine along with some bootstrap and jQuery scripts to
display the Contacts ordererd by their last name.

## Startup

From the prototype's root directory (`/prototypes/ccontacts`) start the SCM
with Maven:

```Shell
$ mvn clean package
$ mvn spring-boot:run
```

Afterwards browse to http://localhost:8080

## Adding Contacts

To add a contact just enter it's information into the form at the top the page 
and hit the `Save``button.

NOTE: All fields are required and a validation check is performed on the email address.

## Browsing Contacts

Browse the Contact list by clicking on the tabs at the top. Tabs without any 
contacts in them are disabled (gray text).

## Deleting Contacts

Delete Contacts by clicking on the trash icon at the right.
