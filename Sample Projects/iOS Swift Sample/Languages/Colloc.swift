/**	This is an automatically generated file	Please don't modify it.	If you need to change some text please do so at	/Users/martinhj/Documents/MobileJazz/OpenSource/Colloc/Sample Projects/iOS Swift Sample/sampleGDocFile.tsv	*/import Foundation

public protocol LocalizedEnum : CustomStringConvertible {}

extension LocalizedEnum where Self: RawRepresentable, Self.RawValue == String {
	public var description : String  {
		return NSLocalizedString(self.rawValue, comment: "")
	}
}

public enum Colloc: String, LocalizedEnum {
	case tr_generic_ok
	case tr_generic_dismiss
	case tr_generic_cancel
	case tr_generic_done
	case tr_generic_back
	case tr_generic_next
}

typealias 🌏 = Colloc
