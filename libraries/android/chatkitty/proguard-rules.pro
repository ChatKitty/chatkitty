-keep class your.package.model.** { *; }

-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

-keepclassmembers class com.chatkitty.example.ChatUIView {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class * {
    @kotlin.Metadata *;
}

-keepclassmembers class kotlinx.coroutines.** {
    <methods>;
}
