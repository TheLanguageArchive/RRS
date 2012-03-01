Resource Request System
-----------------------
The Resource Request System (RRS) is a collection of servlets that
collectively allow users of the archive to register themselves and
subsequently request access to individual resources.

It is integrated with the ImdiBrowser. The 'register' link in the top right
of the browser (e.g.  corpus1) links to the registration page of RRS (e.g. 
on corpus1). Selecting 'request resource access' from the context menu on a
node opens a request form for that node (e.g.  on the DoBeS archive node).

= Registration =

Before being able to make a request, a user needs a registration, i.e. own a
principal record in the AMS database. A user can register either while not
authenticated at all, or authenticate first using his account at a federated
organization. Hence, we can distinguish authenticated registration and
unauthenticated registration.

Unauthenticated registration will require a user to enter a desired username
and password, and manually enter their personal details. After a successful
unauthenticated registration, the user will be able to use their chosen
username and password to login and request access to archive nodes.

Authenticated registration can be carried out by users that already have
credentials with a federated organization. The registration procedure
requires them to enter a form with only personal information (no
username/password fields), that may already be pre-filled depending on the
properties provided by the identity provider (IdP). Registration will create
an AMS principal record for this user, that however will refer to the users
external identity and will not contain a password hash. 

The registration procedure requires the user to enter some personal
information into a form, which gets validated by RRS, and eventually passed
on to Ams2, which will create a new account for the user.

= Resource request =

The request procedure requires the user to select a node to request access
to and fill in his/her motivation for doing so into a form. RSS will then
create a message from this request and sends this to the corpman email
address. Corpus management will then manually process this request.
