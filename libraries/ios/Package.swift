// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "com.chatkitty.ui",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(name: "ChatUi", targets: ["ChatUi"]),
    ],
    dependencies: [
        .package(url: "https://github.com/Moya/Moya.git", .upToNextMajor(from: "15.0.0")),
        .package(url: "https://github.com/daltoniam/Starscream.git", from: "4.0.6"),
        .package(url: "https://github.com/ReactiveX/RxSwift.git", .upToNextMajor(from: "6.0.0"))
    ],
    targets: [
        .target(
            name: "ChatUi",
            dependencies: ["Moya", "Starscream", "RxSwift"],
            resources: [
                .process("WebBridge/js/FlexHybridiOS.js")],
            ),
        .testTarget(
            name: "ChatUiTests",
            dependencies: ["ChatUi"]),
    ]
)
