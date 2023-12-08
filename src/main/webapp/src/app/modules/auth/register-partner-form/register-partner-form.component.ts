import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
import { RegisterPartnerDto } from 'src/app/shared/model/api-models';


function customNameValidator(control: FormControl) {
  // Implement your custom validation logic here

  const forbiddenName = 'admin'; // Example: Forbid the name 'admin'

  if (control.value && control.value.toLowerCase() === forbiddenName) {
    return { forbiddenName: true }; // Validation failed
  }

  return null; // Validation passed
}

@Component({
  selector: 'app-register-partner-form',
  templateUrl: './register-partner-form.component.html',
  styleUrls: ['./register-partner-form.component.scss']
})
export class RegisterPartnerFormComponent implements OnInit {


  partnerForm: FormGroup = new FormGroup({
    //User data
    firstName: new FormControl(''),
    lastName: new FormControl(''),
    telephoneNumber: new FormControl(''),
    email: new FormControl(''),
    password: new FormControl(''),

    //Partner data
    name: new FormControl(''),
    accountNumber: new FormControl(''),
    contactNumber: new FormControl(''),
    address: new FormGroup({
      city: new FormControl(''),
      postalCode: new FormControl(''),
      street: new FormControl(''),
    }),
    
  });
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.partnerForm = this.formBuilder.group({
      firstName: ['', [Validators.required, customNameValidator, Validators.minLength(2),]],
      lastName: [
        '',
        [
          Validators.required,
          Validators.minLength(2),
          Validators.maxLength(20)
        ]
      ],
      telephoneNumber: [
        '',
        [
          Validators.required,
          Validators.pattern(/^\d{9}$/),
        ]
      ],
      email: ['', [Validators.required, Validators.email]],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(20)
        ]
      ],
      name: ['', [Validators.required, Validators.minLength(2)]],
      accountNumber: [
        '',
        [
          Validators.required,
          Validators.pattern(/^\d{26}$/) // Format numeru konta XX XXXX XXXX XXXX XXXX XXXX XXXX
        ]
      ],
      contactNumber: [
        '',
        [
          Validators.required,
          Validators.pattern(/^\d{9}$/),
        ]
      ],
      address: this.formBuilder.group({
        city: ['', [Validators.required, Validators.minLength(2)]],
        postalCode: [
          '',
          [
            Validators.required,
            Validators.pattern(/^\d{2}-\d{3}$/) // Format kodu pocztowego XX-XXX
          ]
        ],
        street: ['', [Validators.required, Validators.minLength(2)]],
      }),
    });
  }
  onSubmit(){
    console.log(JSON.stringify(this.partnerForm.value, null, 2));
    console.log(this.partnerForm)
  }

  get f(): { [key: string]: AbstractControl } {
    return this.partnerForm.controls;
  }
  
  get addressForm(): FormGroup {
    return this.partnerForm.get('address') as FormGroup;
  }
}
