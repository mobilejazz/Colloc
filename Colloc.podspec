#
# Be sure to run `pod lib lint Colloc.podspec' to ensure this is a
# valid spec before submitting.
#
# Any lines starting with a # are optional, but their use is encouraged
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html
#

Pod::Spec.new do |s|
    s.name             = 'Colloc'
    s.version          = '1.0.3'
    s.summary          = 'Collaborative Localizations for iOS and Android using Google Documents'
    
    s.description      = <<-DESC
    Use Goolge drive to define your localization strings and export them to iOS or Android with Colloc, that simple.
    DESC
    
    s.homepage         = 'https://github.com/mobilejazz/Colloc'
    s.license          = { :type => 'Apache License, Version 2.0', :file => 'LICENSE' }
    s.author           = { 'Mobile Jazz' => 'info@mobilejazz.com' }
    s.source           = { :git => 'https://github.com/mobilejazz/Colloc.git', :tag => s.version.to_s }
    s.social_media_url = 'https://twitter.com/mobilejazzcom'
    
    s.resources = ['colloc.php', 'run_script_sample.sh']
end
