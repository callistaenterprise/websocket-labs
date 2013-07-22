SETING UP RPI
-------------

NOOB installation + raspbian (wheezy)

Questions:
1. How to recover SD-card after crasch, fscheck...
2. How to dynamically register IP addresses, or setup static on a known network subnet.
3. How to backup/restore a SD-card (to smaller siza?)
4. Check on ; http://help.directadmin.com/item.php?id=379

Command-tips:
- Shift-key at boot gives the possibility to reinstall the RPi!
- sudo raspi-config
- startx
- boot-messages: /var/log/boot

installera bra att ha tools:
- sudo apt-get install vim
- sudo apt-get install tree
- sudo apt-get install ant
  - add line to "/etc/environment":
    ANT_HOME="/usr/share/ant"
  - add in the the end to "~/.bashrc" 
    export ANT_HOME="/usr/share/ant"

installera ssh-nyckel
- http://code.google.com/p/soi-toolkit/wiki/IG_RT_Public_Key_Cryptography

installera java 8
- Download Java 8: http://java.dzone.com/articles/javafx-raspberry-pi-%E2%80%93-3-easy
  - wget --no-check-certificate http://www.java.net/download/jdk8/archive/b97/binaries/jdk-8-ea-b97-linux-arm-vfp-hflt-03_jul_2013.tar.gz
  - sudo tar -zxvf jdk-8-ea-b97-linux-arm-vfp-hflt-03_jul_2013.tar.gz -C /opt
  - /opt/jdk1.8.0/bin/java -version

- Setting up Java on a RPi: http://www.savagehomeautomation.com/projects/raspberry-pi-installing-oracle-java-development-kit-jdk-170u.html
  - sudo update-alternatives --install "/usr/bin/java" "java" "/opt/jdk1.8.0/bin/java" 1
  - sudo update-alternatives --set java /opt/jdk1.8.0/bin/java  
  - add line to "/etc/environment":
    JAVA_HOME="/opt/jdk1.8.0"
  - add in the the end to "~/.bashrc" 
    export JAVA_HOME="/opt/jdk1.8.0"
    export PATH=$PATH:$JAVA_HOME/bin 

bygg jws, se nedan

WiringPi
- http://wiringpi.com/
  - git clone git://git.drogon.net/wiringPi
  - cd wiringPi
  - ./build
  
- Export GPIO pin #0 to user level programs
  Add the following to /etc/rc.local
# 2013-07-16. ML. Added to export GPIO pin #0 to user level programs
/bin/echo "Running gpio to export pin 17 for output..."
sudo -u pi usr/local/bin/gpio export 17 out
/usr/local/bin/gpio exports


Java GIOP
- https://github.com/jkransen/framboos
  - git clone git@github.com:jkransen/framboos.git
  - mvn install

WS-ONE
------

mvn clean package
chmod 777 target/generated-resources/appassembler/jsw/ws-one-server/bin/ws-one-server
chmod 777 target/generated-resources/appassembler/jsw/ws-one-server/bin/wrapper-macosx-universal-32
mkdir target/generated-resources/appassembler/jsw/ws-one-server/logs

target/generated-resources/appassembler/jsw/ws-one-server/bin/ws-one-server console
target/generated-resources/appassembler/jsw/ws-one-server/bin/ws-one-server start
target/generated-resources/appassembler/jsw/ws-one-server/bin/ws-one-server status
target/generated-resources/appassembler/jsw/ws-one-server/bin/ws-one-server stop

tail -F target/generated-resources/appassembler/jsw/ws-one-server/wrapper.log &



MAKE RELEASE
------------

1. mvn clean package
2. COPY src/main/resources/web to target/generated-resources/appassembler/jsw/ws-one-server
3. ZIP /ws-one/target/generated-resources/appassembler/jsw/ws-one-server to /ws-one/releases (e.g. ws-one-server-1.0.0-M1.zip)


DISTRIBUTE
----------

scp releases/ws-one-server-1.0.0-M1.zip rpi:


INSTALL/UPDATE
--------------

(libwrapper-linux-armhf-32.so
wrapper-linux-armv6l-32, wrapper-linux-armv6hf-32 (?))

cd

# Stop if running
sudo service ws-one-server status
sudo service ws-one-server stop

# Remove previous installation (or backup...)
sudo rm -r /opt/ws-one-server/ 

# Unzip, set privs and create missing folder
unzip ws-one-server-1.0.0-M1.zip
chmod 777 ws-one-server/bin/ws-one-server
mkdir ws-one-server/logs

# Copy locally build JWS-files
cp wrapper_prerelease_3.5.19/dist/wrapper-linux-armhf-32-3.5.19/bin/wrapper ws-one-server/bin/
cp wrapper_prerelease_3.5.19/dist/wrapper-linux-armhf-32-3.5.19/lib/wrapper.jar ws-one-server/lib/
cp wrapper_prerelease_3.5.19/dist/wrapper-linux-armhf-32-3.5.19/lib/libwrapper.so ws-one-server/lib/

# Move to target
sudo mv ws-one-server /opt


REGISTER DEAMON
---------------

One time only!

sudo ln -s /opt/ws-one-server/bin/ws-one-server /etc/init.d/ws-one-server
sudo update-rc.d ws-one-server start 66 2 3 4 5 . stop 66 0 1 6 .

sudo service ws-one-server console
sudo service ws-one-server start
sudo service ws-one-server stop
sudo service ws-one-server status

# sudo /opt/ws-one-server/bin/ws-one-server console
# sudo /opt/ws-one-server/bin/ws-one-server start
# sudo /opt/ws-one-server/bin/ws-one-server status
# sudo /opt/ws-one-server/bin/ws-one-server stop

tail -F /opt/ws-one-server/wrapper.log &

UNINSTALL
---------
# Stop if running
sudo service ws-one-server status
sudo service ws-one-server stop

# Unregister service
sudo update-rc.d -f ws-one-server remove
sudo rm /etc/init.d/ws-one-server

# Remove installation
sudo rm -r /opt/ws-one-server/ 


TODO: E.g. add something like the following to your Main method:

Runtime.getRuntime().addShutdownHook(new Thread(){
    public void run() {
        log.debug("Shutdown hook was invoked. Shutting down App1.");
        App1JmxStopper appJmxStopper = new App1JmxStopper();
        appJmxStopper.stop();
    }
});

JMX
---

JMX parametrar
java -Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=192.168.1.122 com.example.Main

JMX:    
   service:jmx:rmi:///jndi/rmi://192.168.1.120:1099/jmxrmi
   service:jmx:rmi:///jndi/rmi://192.168.1.122:1100/jmxrmi

JWS-wrapper.config settings:

wrapper.java.additional.1=-Dcom.sun.management.jmxremote
wrapper.java.additional.2=-Dorg.tanukisoftware.wrapper.WrapperManager.mbean=TRUE
wrapper.java.additional.3=-Dcom.sun.management.jmxremote.port=1100
wrapper.java.additional.4=-Dcom.sun.management.jmxremote.authenticate=false
wrapper.java.additional.5=-Dcom.sun.management.jmxremote.ssl=false
wrapper.java.additional.6=-Djava.rmi.server.hostname=192.168.1.122


Compiling JSW for the Arm arch on your RPi
------------------------------------------

http://java-service-wrapper.996253.n3.nabble.com/Wrapper-on-Raspberry-PI-td2889.html

Source http://sourceforge.net/projects/wrapper/files/wrapper_prerelease/Wrapper_3.5.19_20130419/
wget http://downloads.sourceforge.net/project/wrapper/wrapper_prerelease/Wrapper_3.5.19_20130419/wrapper_prerelease_3.5.19.tar.gz
tar xvfz wrapper_prerelease_3.5.19.tar.gz
cd wrapper_prerelease_3.5.19
./build32.sh release

cd dist
tar xvfz wrapper-linux-armhf-32-3.5.19.tar.gz 
cd wrapper-linux-armhf-32-3.5.19/

ls -l bin/wrapper 
--> -rwxr-xr-x 1 pi pi 256832 Jul 14 09:25 bin/wrapper

ls -l lib/libwrapper.so 
--> -rwxr-xr-x 1 pi pi 31628 Jul 14 09:25 lib/libwrapper.so

ls -l lib/wrapper.jar 
--> -rw-r--r-- 1 pi pi 119417 Jul 14 09:25 lib/wrapper.jar



WIFI
----

Add on latest wifi driver: http://tech.enekochan.com/2013/05/29/tp-link-tl-wn725n-version-2-in-raspberry-pi/
wifi not detected after warm reboot (sudo reboot) but after a cold reboot (sudo shudown h -now + unpug/plug AC-adpter)
wifi not auto-connected, need to open wpa-gui and click on connect

Auto-start wifi: http://www.savagehomeautomation.com/projects/raspberry-pi-installing-the-edimax-ew-7811un-usb-wifi-adapte.html
Gives: 
sudo vim /etc/network/interfaces 
add the line: auto wlan0

Mer om autostart av wifi (har ej provat): http://svay.com/blog/setting-up-a-wifi-connection-on-the-raspberrypi/

Static IP adress or Mac allocated DHCP adress (better!): http://www.raspberrypi.org/phpBB3/viewtopic.php?f=26&t=42670

