Karaf SCR Examples
---------------------------

These Examples are used for my SCR Components with Karaf Articles:

http://sully6768.blogspot.com/2012/09/scr-components-with-karaf.html

install -s mvn:org.apache.karaf.scr/org.apache.karaf.scr.examples.service/2.2.9
dev:watch mvn:org.apache.karaf.scr/org.apache.karaf.scr.examples.service/2.2.9

karaf@root> scr:list
   ID   State             Component Name
[2   ] [ACTIVE          ] ActivateCommand
[5   ] [ACTIVE          ] GreeterServiceComponent
[3   ] [ACTIVE          ] ListCommand
[6   ] [ACTIVE          ] org.apache.karaf.scr.examples.service.impl.GreeterServiceImpl
[4   ] [ACTIVE          ] ScrServiceMBean
[1   ] [ACTIVE          ] DeactivateCommand
[0   ] [ACTIVE          ] DetailsCommand


2012-09-07 09:34:27,801 | INFO  | l Console Thread | service                          | 55 - org.apache.karaf.scr.examples.service - 2.2.9 | Activating the GreeterService Component
2012-09-07 09:34:27,802 | INFO  | l Console Thread | service                          | 55 - org.apache.karaf.scr.examples.service - 2.2.9 | Hello scenglan

scr:deactivate org.apache.karaf.scr.examples.service.impl.GreeterServiceImpl

{snip}   Deactivating the GreeterService Component


{snip}  Activating the GreeterService Component
{snip}   Hello scenglan


install -s mvn:org.apache.karaf.scr/org.apache.karaf.scr.examples.managed.service/2.2.9
src:list

karaf@root> scr:list 
   ID   State             Component Name
[0   ] [ACTIVE          ] ActivateCommand
[19  ] [UNSATISFIED     ] ManagedGreeterService
[18  ] [UNSATISFIED     ] ManagedGreeterServiceComponent
[12  ] [ACTIVE          ] GreeterServiceComponent
[1   ] [ACTIVE          ] ListCommand
[13  ] [ACTIVE          ] org.apache.karaf.scr.examples.service.impl.GreeterServiceImpl
[4   ] [ACTIVE          ] ScrServiceMBean
[3   ] [ACTIVE          ] DetailsCommand
[2   ] [ACTIVE          ] DeactivateCommand


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
