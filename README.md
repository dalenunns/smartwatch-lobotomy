# Smart Watch Lobotomy

This repo contains scripts and other artifacts for my talk [Smart Watch Lobotomy](https://www.xor.co.za/talks/smartwatch_lobotomy/)

You can find out a lot more information by reading the accompanying [blog post](https://www.xor.co.za/post/2022-11-30-hacking-smartwatch/) that explains in detail how all this works.

## `VibrateCommand.java`

This generates the command value required to make the watch vibrate so that you can find it.

You can compile the file with `javac VibrateCommand.java` and then run it with `java VibrateCommand` and it'll output a hex string you can send the watch using a tool like `gatttool`.

Another option is copying and pasting it into an [online java compiler](https://www.programiz.com/java-programming/online-compiler/) and compile and run it online.

## `MessageConvert.java`

This generates the message notification and the command to switch notifications on.

You can compile the file with `javac MessageConvert.java` and then run it with `java MessageConvert` and it'll output hex strings you can send the watching using a tool like `gatttool`.

Another option is copying and pasting it into an [online java compiler](https://www.programiz.com/java-programming/online-compiler/) and compile and run it online.

## OtaParser

This is a work in progress and just something hacked together to generate OTA Update commands for a particular binary image.

It's very hacky.

**WARNING - BE VERY CAREFUL RUNNING THESE COMMANDS WILL BRICK YOUR WATCH !!!**

### Building

You can build it from the `OtaParser` folder by running `javac -g -d Main.java`

When running it you need to pass it the binary file you want to process, the MAC address and the handle for the OTA Update. It'll then generate `gatttool` commands you could run.

For example `java Main 5316_ble_remote.bin FF:FF:FF:10:48:92 0x003a`

**WARNING - BE VERY CAREFUL RUNNING THESE COMMANDS WILL BRICK YOUR WATCH !!!**