# ChatKitty for iOS

[![License](https://img.shields.io/cocoapods/l/ChatKitty.svg?style=flat)](https://cocoapods.org/pods/ChatKitty)
[![Platform](https://img.shields.io/cocoapods/p/ChatKitty.svg?style=flat)](https://cocoapods.org/pods/ChatKitty)

## Example

To run the example project, clone the [chatkitty](https://github.com/ChatKitty/chatkitty) repo and open the ChatKittyExample.xcodeproj in examples/ios-example.

## Adding ChatKitty to Your Project

1. Open your .xcodeproj file and go to File -> Add Package Dependencies 
2. In the top right search bar, paste https://github.com/ChatKitty/chatkitty-ios.git 
3. Set the "Add to Project" field to your project
4. Click "Add Package"

## Usage

```swift
    let connectionApi = ConnectionApi(apiKey: "YOUR_API_KEY") // Optional to use API Key 
    let configuration = ChatKittyConfiguration(widgetId: "YOUR_WIDGET_ID",
                                            username: "YOUR_USERNAME",
                                            connectionApi: connectionApi,
                                            theme: .light)
    
    let components = ChatKittyComponents(
        onMounted: { context in
            print("Chat UI mounted with context:", context)
        },
        onHeaderSelected: { channel in
            print("Header selected for channel:", channel)
        },
        onMenuActionSelected: { action in
            print("Menu action selected:", action)
        }
    )
    
    let view = ChatKittyView(configuration: configuration, components: components)
                                                
```
Replace `YOUR_API_KEY`, `YOUR_WIDGET_ID`, and `YOUR_USERNAME` with your actual API key, widget ID, and username.

## Customization
ChatKitty supports extensive customization to fit the look and feel of your app. You can customize themes, fonts, colors, and many other aspects of the chat UI. For detailed customization options, refer to the documentation.

## Contributing
We welcome contributions to ChatKitty! If you have suggestions or issues, please file them via GitHub issues. If you'd like to contribute code, please submit a pull request.
