// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "com.chatkitty",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(name: "ChatKitty", targets: ["ChatKitty"]),
    ],
    dependencies: [
        .package(url: "https://github.com/Moya/Moya.git", .upToNextMajor(from: "15.0.0")),
        .package(url: "https://github.com/daltoniam/Starscream.git", from: "4.0.6"),
        .package(url: "https://github.com/ReactiveX/RxSwift.git", .upToNextMajor(from: "6.0.0"))
    ],
    targets: [
        .target(
            name: "ChatKitty",
            dependencies: ["Moya", "Starscream", "RxSwift"],
            resources: [
                .process("WebBridge/js/FlexHybridiOS.js")],
            ),
        .testTarget(
            name: "ChatKittyTests",
            dependencies: ["ChatKitty"]),
    ]
)
