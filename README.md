Karaf SCR Examples
---------------------------

These Examples are used for my SCR Components with Karaf Articles:

http://sully6768.blogspot.com/2012/09/scr-components-with-karaf.html

install -s mvn:org.apache.karaf.scr/org.apache.karaf.scr.examples.service/2.2.9
dev:watch mvn:org.apache.karaf.scr/org.apache.karaf.scr.examples.service/2.2.9

karaf@root> scr:list
   ID   State             Component Name
[5   ] [ACTIVE          ] GreeterServiceComponent
[6   ] [ACTIVE          ] org.apache.karaf.scr.examples.service.impl.GreeterServiceImpl

scr:deactivate org.apache.karaf.scr.examples.service.impl.GreeterServiceImpl

{snip}   Deactivating the GreeterService Component

{snip}  Activating the GreeterService Component
{snip}   Hello scenglan


install -s mvn:org.apache.karaf.scr/org.apache.karaf.scr.examples.managed.service/2.2.9
src:list

karaf@root> scr:list 
   ID   State             Component Name
[19  ] [UNSATISFIED     ] ManagedGreeterService
[18  ] [UNSATISFIED     ] ManagedGreeterServiceComponent


config:edit ManagedGreeterService
config:propset salutation "Hi there"
config:propset name "Scott"
config:update

In our log file:
{snip}  Activating the Managed Greeeter Service
{snip}  Activating the Managed GreeterService Component
{snip}  Hi there Scott
{snip}  Installed /opt/karaf/apache-karaf-2.2.9/etc/ManagedGreeterService.cfg

src:list

karaf@root> scr:list 
   ID   State             Component Name
{snip}
[19  ] [ACTIVE          ] ManagedGreeterService
[18  ] [ACTIVE          ] ManagedGreeterServiceComponent
{snip}


config:delete ManagedGreeterService

config:edit ManagedComponent
config:update

config:edit MetaTypeManagedComponent
config:propset salutation Hello
config:propset name Marla
config:update
