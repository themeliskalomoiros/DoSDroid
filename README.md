# DoSdroid

An Android application which makes it possible to organize and launch Denial of Service (DoS) attacks at HTTP Servers,
on specific date and time. 

The true purpose of this app is to demonstrate the power which comes when Android devices are working for a single purpose. 
In this case that is accomplished through device connectivity. 

As a result an interest was taken on the various device interactions that are possible through the standard network connectivity
but with protocols such as Bluetooth, NFC, Wi-Fi P2Pand Bluetooth as well.

![The DosDroid](https://github.com/tomasmichael995/DoSDroid/blob/job_schedule/robot-line.png)

**This app may be used only for educational purposes!** 

## Getting Started
Download this repository and run the app!

### Prerequisites

-Tools to install the application, (Android Studio, Eclipse, others).

-At least two devices running Android (API level 14 and above).

-Internet connection.

## How to use DoSdroid

There are two use cases. 
Creating or joining an attack (both are also possible, but as seperate use cases).

### Creating an Attack
By creating an Attack the user's device acts as a server where client connections are possible through
a specific network configuration per attack. 

For example the server device may choose to gather it's botnet over *Bluetooth or Wifi Peer to Peer*.
That means that the users who wish to join the server's attack they must first establish a connection with
the server device through that specific network configuration.

The procedure is,

1. Start the app and click the menu option to attack a website.

2. Set the target website by pasting its valid URL. 

3. Set date and time to draw the start line of the attack. 

4. Choose one of the available network configuration options. 

5. Hit create!

### Joining an Attack
One may want to join one of the already declared attacks. 
For a succesfull join the device must connect to the server first. 

After a succesful connection it will aqcuire critical information about the attack and will set itself
ready for the upcoming launch.

The procedure to join is,

1.  Start the app and click the menu option to join an attack.

2. Choose which target you want to join attacking.

3. Hit join!

At this point, depending on the network configuration of the server, the user may be asked
to provide more information about the connectivity.

```
For example, if the creator of the attack chose Bluetooth as the network configuration, then the
user will be asked to enable the Bluetooth on the device.
```

## Device Resources

### After the Connection
The *user who joined* one or more attacks doesn't have to worry about resource draining from his device.

The connection with the server lasts as long as it gets the response. Then it validates and closes the 
connection, waiting for the attack's launch.

On the other hand, the *one who created the attack* should know that there will be resource draining from
its device. 

That's inevitable because his device is technically a server which *always listens* 
to connection requests from clients.

> Note that when the server disconnects (due to user's intention or an error) then all the ones who subscribed for 
> that attack will be effectivelly unsubscribed! It doesn't matter if the client devices were not connected with the
> server during its dissconnection.

### During the Attack

When the attack is launch every subscribed client device starts to call the target server. 

The *subscribed users* will be notified through notification when the attack had begun. 
Their network activity will start to rise and their battery will start to drain. That is the price to pay!

But of course the resource draining is analogous of the attacks that are happening in that time.
Good thing there is the option to abandon an attack, or all at once.

## Built With

* Java
* Firebase
* [MVC](https://www.techyourchance.com/mvp-mvc-android-1/) - App architecture

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
