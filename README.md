Karaf SCR Examples
---------------------------

These Examples are used for my SCR Components with Karaf Articles.

I use the Karaf SCR Commands for all my OSGi related examples.  OSGi Declarative Service or the Service Component Runtime (SCR) is a very lightweight and easy to use framework that is suitable for dependency injection.  It has been available as part of the OSGi Specification since 4.0 making it well baked and stable.  While it uses XML for the wiring at runtime the XMl can actually be generated at build time using either the BND or Felix Annotations.  They are both solid and stable implementations but have some significant differences:

Felix SCR Annotations

* Inheritance Support for abstract components
* Generated bind/unbind methods (less code by you, more code by manipulation)

BND/OSGi DS Annotations
* Boilerplate for the new OSGi DS Annotations in the 4.3 Release
* Fully supported by the Maven Bundle Plugin
* No other library dependencies (Felix requires the annotation lib be installed)

I personally prefer the BND/OSGi annotations.  They are lighter weight with regards to development and deployment.  I can use my Maven Bundle Plugin stand alone and not have to introduce a new plugin and configuration.  I also don't have to install the Felix Annotations into my container.  Finally, my source no longer matches the generated binary.

My concerns above are minimal with the exception of the generated code.  Even if you use the Felix annotations I would avoid using this feature.  Remember that with SCR you are dynamically obtaining reference to your services and that these are concrete references and not proxies (another big plus of SCR over other DI frameworks).  You have to ensure thread safety on these references and the use of ReadWriteLocks is far superior to syncronization blocks.  More on that later though. 

The one thing I would love to have though is inheritance.  That is still a big negative but it is being discussed over at the OSGi group.

Apache Felix Maven SCR - http://felix.apache.org/site/apache-felix-maven-scr-plugin.html
BND Annotation - http://www.aqute.biz/Bnd/Components


To get started install Karaf 2.2.9.  It can be found here:

https://repository.apache.org/content/groups/public/org/apache/karaf/apache-karaf/2.2.9/

Hint:
I live by tail so to make my life easier I remove the ${karaf.data}/ from the etc/org.ops4j.pax.logging.cfg appender directories so I can just delete the data dir without restarting tail.

To get started with Karaf SCR, first install the SCR Feature with the SCR Commands.  This will allow you to manipulate the installed examples at runtime.

Navigate to where you installed Karaf 2.2.9 and open the etc/org.ops4j.pax.url.mvn.cfg file.  This is where the configuration for Maven repository lookups are kept.  Navigate to the bottom of the file and add the following repository:

http://sully6768.github.com/repos/releases/

This is where Karaf will find the SCR Command binaries for 2.2.x.

Now start Karaf:

First ./bin/karaf debug

Once it is up and running we can add the SCR Feature:

features:addurl mvn:org.apache.karaf.scr/org.apache.karaf.scr.feature/2.2.9/xml/features
features:install scr

If you execute list -t 0 your output should end with the following four lines:

<pre  style="font-family:arial;font-size:12px;border:1px dashed #CCCCCC;width:99%;height:auto;overflow:auto;background:#f0f0f0;padding:0px;color:#000000;text-align:left;line-height:20px;"><code style="color:#000000;word-wrap:normal;">karaf@root&gt; ll  
 START LEVEL 100 , List Threshold: 50  
   ID  State     Blueprint   Level Name  
 {snip...}  
 [ 51] [Active   ] [      ] [  80] Apache Felix Metatype Service (1.0.4)  
 [ 52] [Active   ] [      ] [  80] Apache Felix Declarative Services (1.6.0)  
 [ 53] [Active   ] [      ] [  80] Apache Karaf :: SCR :: Shell Commands (2.2.9)  
 [ 54] [Active   ] [      ] [  80] Apache Karaf :: SCR :: Management MBeans (2.2.9)  
 karaf@root&gt;</code></pre>

<pre style="font-family:arial;font-size:12px;border:1px dashed #CCCCCC;width:99%;height:auto;overflow:auto;background:#f0f0f0;padding:0px;color:#000000;text-align:left;line-height:20px;"><code style="color:#000000;word-wrap:normal;">./bin/karaf debug</code></pre>


Also, reviewing the karaf.log file you will see the floowing log output if you have it set to INFO (I have trimmed the log output to only reflect the import parts):<br />
<pre style="font-family:arial;font-size:12px;border:1px dashed #CCCCCC;width:99%;height:auto;overflow:auto;background:#f0f0f0;padding:0px;color:#000000;text-align:left;line-height:20px;"><code style="color:#000000;word-wrap:normal;">INFO  | Activating the Apache Karaf SCR Deactivate Command
INFO  | Activating the Apache Karaf SCR Activate Command
INFO  | Activating the Apache Karaf SCR List Command
INFO  | Activating the Apache Karaf SCR Details Command
INFO  | Activating the Apache Karaf SCR Service MBean</code></pre>
<br />

The commands we have installed installed are
scr:activate
scr:deactivate
scr:details
scr:list


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



<pre  style="font-family:arial;font-size:12px;border:1px dashed #CCCCCC;width:99%;height:auto;overflow:auto;background:#f0f0f0;padding:0px;color:#000000;text-align:left;line-height:20px;">
<code style="color:#000000;word-wrap:normal;">
</code>
</pre>
