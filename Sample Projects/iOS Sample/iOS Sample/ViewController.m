//
//  ViewController.m
//  iOS Sample
//
//  Created by Joan Martin on 02/02/15.
//  Copyright (c) 2015 Mobile Jazz. All rights reserved.
//

#import "ViewController.h"

// You can also put this import inside a Precompiled header (.PCH) file
// for your project. This way, you don't have to import it every time
// learn more: http://useyourloaf.com/blog/modules-and-precompiled-headers/

#import "Localization.h"


@interface ViewController ()
@property (weak, nonatomic) IBOutlet UILabel *okLabel;
@property (weak, nonatomic) IBOutlet UILabel *dismissLabel;
@property (weak, nonatomic) IBOutlet UILabel *cancelLabel;


@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Now use Colloc to assign those texts!
    
    self.okLabel.text = tr_generic_ok;
    self.dismissLabel.text = tr_generic_dismiss;
    self.cancelLabel.text = tr_generic_cancel;
}

@end
