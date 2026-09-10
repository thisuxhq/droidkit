# Empty state

Patterns are more opinionated than primitives. One title, one description, at most one primary action.

## EmptyState.kt

```kotlin
@Composable
fun EmptyState(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    action: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        if (action != null && onAction != null) {
            Spacer(Modifier.height(4.dp))

            AppButton(
                text = action,
                onClick = onAction
            )
        }
    }
}
```

## Usage

```kotlin
EmptyState(
    title = "No projects yet",
    description = "Create your first project to get started.",
    action = "Create project",
    onAction = {
        createProject()
    }
)
```
