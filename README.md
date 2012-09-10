Karaf SCR Examples
---------------------------

I use the Karaf SCR Commands for all my OSGi related examples.  OSGi Declarative Service or the Service Component Runtime (SCR) is a very lightweight and easy to use framework that is suitable for dependency injection.  It has been available as part of the OSGi Specification since 4.0 and is well baked and stable.  While it uses XML for the wiring at runtime the XMl can actually be generated at build time using either the BND or Felix Annotations.  They are both solid and stable implementations but have some significant differences:

Felix SCR Annotations

* Inheritance Support for abstract components
* Generated bind/unbind methods  

BND/OSGi DS Annotations
* Boilerplate for the new OSGi DS Annotations in the 4.3 Release
* Fully supported by the Maven Bundle Plugin

The BND/OSGi DS Annotations offer a lightweight SCR XML generation at build time.

The biggest benefit between BND Annotations are being integrated into the OSGi Specification and are supported by the Maven Bundle Plugin.  You simply have to add the <Service-Component>*</Service-Component> element to your maven-bundle-plugin configuration and it will pick up any annotated component.  It also has support for OSGi Metatype if the BND Metatype Annotations are found.  With all that goodness the BND annotations have a major drawback in that it doesn't support inheritance.

The Felix Annotations have some good benefits as well.  They allow for more configurations over code by require a separate Mavan Plugin which is not a major concern

I prefer the BND Annotations for two reasons.  First they are supported by the Maven Bundle Plugin while the Felix Annotations 

Apache Felix Maven SCR - http://felix.apache.org/site/apache-felix-maven-scr-plugin.html
BND Annotation - http://www.aqute.biz/Bnd/Components

Therefore I felt it was probably a good idea to include a primer on SCRThere are lots of examples around the Internet on how to implement .   
This guide is more of a 

I am a big fan of SCR due to its simplicity and lightweight nature.  

First lets install the SCR Feature that will install commands 
features:addurl mvn:org.apache.karaf.scr/org.apache.karaf.scr.feature/2.2.9/xml/features
features:install scr

INFO  | Activating the Apache Karaf SCR Details Command
INFO  | Activating the Apache Karaf SCR Deactivate Command
INFO  | Activating the Apache Karaf SCR Activate Command
INFO  | Activating the Apache Karaf SCR List Command
INFO  | Activating the Apache Karaf SCR Service MBean

scr:activate      scr:deactivate    scr:details       scr:list


karaf@root> scr:list 
   ID   State             Component Name
karaf@root>

karaf@root> scr:list -s 
   ID   State             Component Name
[2   ] [ACTIVE          ] ActivateCommand
[3   ] [ACTIVE          ] ListCommand
[4   ] [ACTIVE          ] ScrServiceMBean
[1   ] [ACTIVE          ] DeactivateCommand
[0   ] [ACTIVE          ] DetailsCommand
karaf@root>


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