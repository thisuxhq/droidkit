# Introduction

DroidKit is the open UI system for building polished Android apps.

It is also: beautiful, production-ready Compose components that you own.

## What this is not

It is not “Shadcn for Android.”

That line is useful for five seconds. It is a bad long-term identity. Shadcn proved that **open code + composition + a registry** is a better distribution model than a versioned component package. DroidKit uses that model. The product is Android product UI, not a port of a web library.

It is not a Maven artifact you `implementation()` and never open.

It is not a replacement for Compose or Material 3. Those stay underneath.

## The problem

An Android developer today gets this stack:

```text
Jetpack Compose
        ↓
Material 3
        ↓
Button, Card, Dialog, BottomSheet,
TextField, NavigationBar, …
```

Those are good primitives.

A real product also needs:

```text
Empty state          Settings row         OTP input
Search screen        Profile selector     Avatar stack
Expandable cards     Loading states       Paywall
Subscription picker  AI chat              Chat composer
Attachment picker    Command palette      Filter sheet
Date selector        Onboarding           Permission request
Error recovery       Floating actions     Swipe actions
Skeleton loaders     Media viewer         …
```

Developers rebuild these in every app. The primitives are not the gap. Product-quality UI is the gap.

## The bet

Google supports [custom design systems on top of Compose](https://developer.android.com/develop/ui/compose/designsystems/custom), including extending or replacing parts of Material. Android UI development is now [Compose First](https://android-developers.googleblog.com/2026/05/android-ui-development-is-compose-first.html).

That is the window:

**Material gives developers primitives. DroidKit gives them product-quality UI.**

## The short pitch

> Build Android apps that feel designed, not assembled.

Then:

> Beautiful, accessible, and production-ready Jetpack Compose components. Copy them into your project and make them yours.

Developer shorthand, when you need it:

> shadcn-style open components for Android, with stronger UX opinions.

## Who it is for

Android developers shipping product UI in Jetpack Compose — especially people who are tired of restyling Material for the tenth time, and coding agents that need a design system they can actually query.

XML is out of scope. A huge proprietary runtime is out of scope. V0 is Compose-only, open-code registry, and a small set of excellent components and patterns.
