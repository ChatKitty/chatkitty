import Foundation
import Moya

enum StompXStream {
    case uploadImages(url: URL, grant: String, images: [UIImage])
    case uploadFiles(url: URL, grant: String, data: [CreateDataFile])
}

extension StompXStream: TargetType {
    var baseURL: URL {
        switch self {
        case let .uploadImages(url, _, _):
            return url
        case let .uploadFiles(url, _, _):
            return url
        }
    }
    
    var path: String {
        ""
    }
    
    var method: Moya.Method {
        switch self {
        case .uploadImages, .uploadFiles:
            return .post
        }
    }
    
    var task: Task {
        switch self {
        case let .uploadImages(_, _, images):
            var array:[MultipartFormData] = []
            for image in images {
                if let pngData = image.jpegData(compressionQuality: 1.0) {
                    let pngData = MultipartFormData(provider: .data(pngData),
                                                    name: "file",
                                                    fileName: "file.png",
                                                    mimeType: "image/png")
                    array.append(pngData)
                }
            }
            return .uploadMultipart(array)
        case let .uploadFiles(_, _, data):
            var array:[MultipartFormData] = []
            for item in data {
                let dataToUpload = MultipartFormData(provider: .data(item.data),
                                                     name: "file",
                                                     fileName: item.name ?? "file",
                                                     mimeType: item.contentType)
                array.append(dataToUpload)
            }
            return .uploadMultipart(array)
        }
    }
    
    var headers: [String : String]? {
        switch self {
        case let .uploadImages(_, grant, _), let .uploadFiles(_, grant, _):
            return ["Content-Type": "multipart/form-data",
             "Grant": grant]
        }
    }
}
