//
//  DeviceInfo.swift
//  FlexHybridApp
//
//  Created by 황견주 on 2021/11/25.
//  Copyright © 2021 황견주. All rights reserved.
//

import Foundation
import UIKit

struct DeviceInfo {
    
    //-- https://www.theiphonewiki.com/wiki/Models
    
    static func getInfo() -> [String:String] {
        var info: [String:String] = [:]
        info["os"] = UIDevice.current.systemName
        info["model"] = deviceModelName()
        info["version"] = UIDevice.current.systemVersion
                
        return info
    }
    
    
    static private func deviceModelName() -> String {
        
        let model = UIDevice.current.model
        
        switch model {
        case "iPhone":
            return self.iPhoneModel()
        case "iPad", "iPad mini":
            return self.iPadModel()
            
        default:
            return "Unknown_Model_\(model)"
        }
    }
    
    static private func getDeviceIdentifier() -> String {
        var systemInfo = utsname()
        uname(&systemInfo)
        let machineMirror = Mirror(reflecting: systemInfo.machine)
        let identifier = machineMirror.children.reduce("") { identifier, element in
            guard let value = element.value as? Int8, value != 0 else { return identifier }
            return identifier + String(UnicodeScalar(UInt8(value)))
        }
                        
        return identifier
    }
    
    
    static private func iPhoneModel() -> String {
        
        let identifier = self.getDeviceIdentifier()
        
        switch identifier {
        case "iPhone7,2" :
            return "iPhone6"
        case "iPhone7,1" :
            return "iPhone6_Plus"
        case "iPhone8,1" :
            return "iPhone6s"
        case "iPhone8,2" :
            return "iPhone6s_Plus"
        case "iPhone8,4" :
            return "iPhone_SE"
        case "iPhone9,1", "iPhone9,3" :
            return "iPhone7"
        case "iPhone9,2", "iPhone9,4" :
            return "iPhone7_Plus"
        case "iPhone10,1", "iPhone10,4" :
            return "iPhone8"
        case "iPhone10,2", "iPhone10,5" :
            return "iPhone8_Plus"
        case "iPhone10,3", "iPhone10,6" :
            return "iPhoneX"
        case "iPhone11,2" :
            return "iPhoneXs"
        case "iPhone11,6" :
            return "iPhoneXs_Max"
        case "iPhone12,1" :
            return "iPhone11"
        case "iPhone12,3" :
            return "iPhone11_Pro"
        case "iPhone12,5" :
            return "iPhone11_Pro_Max"
        case "iPhone12,8" :
            return "iPhone_SE_2nd_Generation"
        case "iPhone13,1":
            return "iPhone_12_Mini"
        case "iPhone13,2":
            return "iPhone_12"
        case "iPhone13,3":
            return "iPhone_12_Pro"
        case "iPhone13,4":
            return "iPhone_12_Pro_Max"
        case "iPhone14,4":
            return "iPhone_13_Mini"
        case "iPhone14,5":
            return "iPhone_13"
        case "iPhone14,2":
            return "iPhone_13_Pro"
        case "iPhone14,3":
            return "iPhone_13_Pro_Max"
        case "iPhone14,6":
            return "iPhone_SE_3rd_Generation"
        default:
            return "Unknown_iPhone_\(identifier)"
        }
    }
    

    static private func iPadModel() -> String {
        
        let identifier = self.getDeviceIdentifier()
        
        switch identifier {
        case "iPad1,1":
            return "iPad"
        case "iPad2,1", "iPad2,2", "iPad2,3", "iPad2,4" :
            return "iPad2"
        case "iPad3,1", "iPad3,2", "iPad3,3" :
            return "iPad_3rd_Generation"
        case "iPad3,4", "iPad3,5", "iPad3,6" :
            return "iPad_4rd_Generation"
        case "iPad6,11", "iPad6,12" :
            return "iPad_5th_Generation"
        case "iPad7,5", "iPad7,6" :
            return "iPad_6th_Generation"
        case "iPad7,11", "iPad7,12" :
            return "iPad_7th_Generation"
        case "iPad11,6", "iPad11,7" :
            return "iPad_8th_Generation"
        case "iPad12,1", "iPad12,2" :
            return "iPad_9th_Generation"

        case "iPad2,5", "iPad2,6", "iPad2,7" :
            return "iPad mini"
        case "iPad4,4", "iPad4,5", "iPad4,6" :
            return "iPad mini2"
        case "iPad4,7", "iPad4,8", "iPad4,9" :
            return "iPad mini3"
        case "iPad5,1", "iPad5,2" :
            return "iPad mini4"
        case "iPad11,1", "iPad11,2" :
            return "iPad mini5"
        case "iPad14,1", "iPad14,2" :
            return "iPad mini6"

        case "iPad4,1", "iPad4,2", "iPad4,3" :
            return "iPad_Air"
        case "iPad5,3", "iPad5,4" :
            return "iPad_Air2"
        case "iPad11,3", "iPad11,4" :
            return "iPad_Air3"
        case "iPad13,1", "iPad13,2" :
            return "iPad_Air4"
        case "iPad13,16", "iPad13,17" :
            return "iPad_Air5"
            
        case "iPad6,7", "iPad6,8" :
            return "iPad_Pro_12.9"
        case "iPad6,3", "iPad6,4" :
            return "iPad_Pro_9.7"
        case "iPad7,1", "iPad7,2" :
            return "iPad_Pro_12.9_2nd_Generation"
        case "iPad7,3", "iPad7,4" :
            return "iPad_Pro_10.5"
        case "iPad8,1", "iPad8,2", "iPad8,3", "iPad8,4" :
            return "iPad_Pro_11"
        case "iPad8,5", "iPad8,6", "iPad8,7", "iPad8,8" :
            return "iPad_Pro_12.9_3rd_Generation"
        case "iPad8,9", "iPad8,10" :
            return "iPad_Pro_11_2nd_Generation"
        case "iPad8,11", "iPad8,12" :
            return "iPad_Pro_12.9_4th_Generation"
        case "iPad13,4", "iPad13,5", "iPad13,6", "iPad13,7" :
            return "iPad_Pro_11_3rd_Generation"
        case "iPad13,8", "iPad13,9", "iPad13,10", "iPad13,11" :
            return "iPad_Pro_12.9_5th_Generation"
            
        default:
            return "Unknown_iPad_\(identifier)"
        }
    }

}
