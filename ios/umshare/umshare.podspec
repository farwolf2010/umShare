

Pod::Spec.new do |s|

 

  s.name         = "umshare"
  s.version      = "0.0.1"
  s.summary      = "Handle some data."
  s.description  = <<-DESC
                    Handle the data.
                   DESC

  s.homepage     = "http://csdn.net/veryitman"
  s.license      = "MIT"
  s.author             = { "veryitman" => "veryitman@126.com" }
  s.source =  { :path => '.' }
  s.source_files  = "Source", "**/**/*.{h,m,mm,c}"
  s.resources = '*.bundle'
  s.ios.vendored_libraries = '*.a'
  s.ios.vendored_frameworks = '*.framework'

  s.exclude_files = "Source/Exclude"
 
  s.dependency 'farwolf.weex'  
  s.dependency 'UMCCommon'
  s.dependency 'UMCSecurityPlugins'
  s.dependency 'UMCShare/UI'
  s.dependency 'UMCShare/Social/WeChat'
  s.dependency 'UMCShare/Social/QQ'
  s.dependency 'UMCShare/Social/Sina'
  #s.dependency 'WechatOpenSDK', '1.8.2'


  s.platform  = :ios, "8.0"
  
  #s.frameworks = 'SystemConfiguration', 'CoreTelephony', 'UIKit', 'Foundation', 'CFNetwork','Security'
  #s.libraries = "z", "sqlite3.0"
  #s.requires_arc  = true


end
