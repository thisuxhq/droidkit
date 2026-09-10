# AI

Coding agents are a primary user. Design the registry, the CLI, and the docs so an agent can search, install, and compose DroidKit without scraping HTML.

Shadcn is already leaning into AI-readable registries and [MCP support](https://ui.shadcn.com/docs/changelog/2025-08-cli-3-mcp). For DroidKit this is core architecture, not a plugin.

## The job to be done

Someone in Cursor, Gemini, Claude, or another agent says:

> Build me a settings screen using DroidKit.

The agent should be able to:

```text
search components
get component
get examples
get API
get design rules
install component
```

without guessing from training data.

## MCP

Expose an MCP server that wraps the registry:

```text
search_components("chat")
get_component("ai-composer")
get_example("settings-screen")
get_design_rules()
```

Same payloads as `droidkit search` / `droidkit view`. One schema, two clients.

Keep tools small and boring. Agents fail when tools try to be clever. Return files, metadata, and examples. Let the agent write the screen.

## Knowledge in the schema

Do not store only Kotlin. Store **when** to use the thing.

```json
{
  "name": "empty-state",
  "type": "pattern",
  "description": "Use when a collection has no content.",
  "avoidWhen": "Do not use for loading or errors.",
  "aiHints": [
    "Prefer inside lazy list containers",
    "Provide one primary action maximum"
  ]
}
```

`description`, `avoidWhen`, and `aiHints` are how you stop an agent from putting an empty state on a loading screen. That is stronger than code search.

## Design rules endpoint

`get_design_rules()` should return a short, stable document:

- Token names and how to read `AppTheme`
- The four catalog categories and when to use each
- Copy rules (empty states, destructive confirms, verb-first actions)
- Accessibility non-negotiables
- “Do not invent a parallel API. Use the installed composables.”

Update this when the system changes. Agents will cache whatever you published last.

## llms.txt and docs

Publish:

- `/llms.txt` — map of the system, links to markdown docs
- These `docs/` files as raw markdown
- Per-item markdown generated from registry JSON + examples

Do not make the website the only readable form. HTML playgrounds are for humans. Markdown is for agents.

## Composition

Agents will ask for screens, not buttons. Recipes and blocks are the right grain:

```text
droidkit add settings
droidkit add recipe:ai-chat
```

The relationship graph in the registry is what lets an agent pull a coherent set instead of a random handful of primitives.

## Instructions at the point of use

`droidkit init` writes a `## DroidKit` section into the consumer project's `AGENTS.md` (or `.agents/skills/droidkit/SKILL.md`):

- The prefix and package in use (`AppButton`, `com.acme.app.ui.components`)
- The four categories and where each lives
- "Use installed components before writing new ones; run `droidkit search` / `droidkit view` first"
- Pointer to `get_design_rules()` via MCP

`droidkit add` appends the item to the installed list in that section. Agents working in that repo then compose from what exists instead of reinventing a button. See [Decisions #12](decisions.md).

## What not to do

- Do not train a custom model as a v0 feature
- Do not hide APIs behind “ask the chatbot”
- Do not generate components on the fly that are not in the registry. The catalog is curated. Agents install from it; they do not extend it unless the user asks to edit the copied files
- Do not let MCP write files by itself if the CLI already does. Prefer `droidkit add` so humans and agents share one install path
