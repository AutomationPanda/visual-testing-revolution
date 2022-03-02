# A Visual Testing Revolution

This is the companion project for
[Pandy Knight](https://twitter.com/AutomationPanda)'s talk, *A Visual Testing Revolution*.


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
