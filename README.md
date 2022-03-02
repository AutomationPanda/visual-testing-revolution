# A Visual Testing Revolution

This is the companion project for
[Pandy Knight's](https://twitter.com/AutomationPanda) talk, *A Visual Testing Revolution*.
It is a small but complete test automation project written in Java.
It contains a traditional functional test
using [Selenium WebDriver](https://www.selenium.dev/) for a demo web app,
as well as a visual test for the same app using
[Applitools Visual AI](https://applitools.com/applitools-ai-and-deep-learning/).

Although this project uses Selenium WebDriver with Java,
Applitools provides [SDKs](https://applitools.com/tutorials/)
for several tools (Selenium, Cypress, Playwright, and more)
and several languages (Java, JavaScript, C#, Python, Ruby, and more).
You can follow the techniques shown in this project for the tool and language of your choice!


## Abstract

Testing is interaction plus verification.
That’s it – you do something, and you make sure it works.
You can perform those two parts manually or with automation.
An automated test script still requires manual effort, though:
someone needs to write code for those interactions and verifications.
For web apps, verifications can be lengthy.
Pages can have hundreds of elements,
and teams constantly take risks when choosing which verifications to perform and which to ignore.
Traditional assertions are also inadequate for testing visuals, like layout and colors.
That’s lots of work for questionable protection. 

There’s a better way: automated visual testing.
Instead of writing several assertions explicitly,
we can take visual snapshots of our pages and compare them over time to detect changes.
If a picture is worth a thousand words, then a snapshot is worth a thousand assertions.
In this talk, I’ll show you how to do this type of visual testing with Applitools.
We’ll automate a basic web UI test together using traditional techniques with Selenium WebDriver and Java,
and then we’ll supercharge it with visual snapshots.
We’ll see how Applitools Visual AI can pinpoint meaningful differences instead of insignificant noise.
We’ll also see how Applitools Ultrafast Test Cloud can render those snapshots
on any browser configuration we want to test without needing to rerun our tests in full.
By the end of this talk, you’ll see how automated visual testing will revolutionize functional test automation!


## Prerequisites

To run the tests in this project, you will need:

1. An Applitools account
   (register [here](https://auth.applitools.com/users/register) for free)
2. [Java Development Kit](https://www.oracle.com/java/technologies/downloads/) (JDK) 17
3. A Java IDE like [IntelliJ IDEA](https://www.jetbrains.com/idea/)
4. Up-to-date local browsers of your choice (Chrome, Firefox, Edge, Safari)
5. Corresponding [browser drivers](https://www.selenium.dev/documentation/webdriver/getting_started/install_drivers/)
   for each target browser

Tests use [Google Chrome](https://www.google.com/chrome/) as the default browser.


## Architecture

This project is a small Java test automation project
containing [JUnit 5](https://junit.org/junit5/) test cases
for an [Applitools demo site](https://demo.applitools.com).
It uses [Selenium WebDriver](https://www.selenium.dev/documentation/webdriver/) for browser interactions
and [Apache Maven](https://search.maven.org/) for dependency management.
Each test case covers the same login behavior, but they do so in different ways:

1. `TraditionalTest` covers login using traditional assertions on the local machine.
2. `UltrafastVisualTest` covers login using Visual AI with [Applitools Eyes](https://applitools.com/products-eyes/)
   and [Ultrafast Grid](https://applitools.com/product-ultrafast-test-cloud/).


## Running tests

The easiest way to run the tests is one at a time through an IDE.
Alternatively, you can run the tests from the command line with Maven using the `mvn test` command.

`TraditionalTest` runs WebDriver sessions on the local machine.
Each test launch can target either Google Chrome or Mozilla Firefox.
Set the `BROWSER` environment variable to `chrome`, `firefox`, `edge`, or `safari` to choose the browser.
If `BROWSER` is not set, the test will default to `chrome`.

`UltrafastVisualTest` runs one WebDriver session on the local machine with Applitools Eyes.
Then, it sends snapshots of pages to Applitools Ultrafast Grid to visually test across seven unique configurations.
To connect to the Applitools cloud,
you must set the `APPLITOOLS_API_KEY` environment variable to your Applitools API key.

Both tests can cover the "original" state of the demo site as well as a visually "changed" state.
Set the `DEMO_SITE` environment variable to `original` or `changed` to pick the target site.
`TraditionalTest` should pass for both versions of the site.
`UltrafastVisualTest` should detect visual differences.
Run it first with `DEMO_SITE=original` to set a baseline,
and then run it with `DEMO_SITE=changed` to reveal the differences.