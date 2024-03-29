import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { RegisterPartnerDto,RegisterResponseDto } from 'src/app/shared/model/api-models';
import { ToastService } from 'src/app/shared/toast/toast.service';


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
    type: new FormControl(''),
    photo: new FormControl(''),
    description: new FormControl(''),
    openHour: new FormControl(''),
    closeHour: new FormControl(''),
    websiteLink: new FormControl(''),
    expectedWaitingTime: new FormControl(''),
    category: new FormControl(''),
    address: new FormGroup({
      city: new FormControl(''),
      postalCode: new FormControl(''),
      street: new FormControl(''),
    }),

  });
  submitted = false;
  wrongEmail: boolean = false;
  wrongPhoto: boolean = false;
  phonePrefixes = ['+48 PL','+355 AL','+376 AD','+43 AT','+375 BY','+32 BE','+387 BA','+359 BG','+385 HR','+357 CY',
  '+420 CZ','+45 DK','+372 EE','+358 FI','+33 FR','+49 DE','+30 GR','+36 HU','+354 IS','+353 IE','+39 IT','+383 XK',
  '+371 LV','+423 LI','+370 LT','+352 LU','+356 MT'];
  imageUrl: string | ArrayBuffer | null | undefined;
  selectedFile: File | null = null;
  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    window.scrollTo(0, 0);

    this.partnerForm = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.minLength(2),
        Validators.pattern(/^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+$/)]],
      lastName: [
        '',
        [
          Validators.required,
          Validators.minLength(2),
          Validators.maxLength(20),
          Validators.pattern(/^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+$/)
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
      description: ['', Validators.required],
      openHour: ['', [Validators.required, Validators.pattern(/^([01]?[0-9]|2[0-3]):[0-5][0-9]$/)]],
      closeHour: ['', [Validators.required, Validators.pattern(/^([01]?[0-9]|2[0-3]):[0-5][0-9]$/)]],
      websiteLink: ['', Validators.required],
      expectedWaitingTime: ['', Validators.required],
      accountNumber: [
        '',
        [
          Validators.required,
          Validators.pattern(/^\d{26}$/) // Format numeru konta XX XXXX XXXX XXXX XXXX XXXX XXXX
        ]
      ],
      numpref: ['+48 PL', Validators.required],
      conpref: ['+48 PL', Validators.required],
      contactNumber: [
        '',
        [
          Validators.required,
          Validators.pattern(/^\d{9}$/),
        ]
      ],
      type:['', [Validators.required]],
      photo:['', [Validators.required]],
      address: this.formBuilder.group({
        city: ['', [Validators.required, Validators.minLength(2)]],
        postalCode: [
          '',
          [
            Validators.required,Validators.minLength(6),Validators.maxLength(6),
            Validators.pattern(/^\d{2}-\d{3}$/) // Format kodu pocztowego XX-XXX
          ]
        ],
        street: ['', [Validators.required, Validators.minLength(2)]],
      }),
    });
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
    this.imageUrl = event.target?.result;
    if(this.selectedFile )
    {
      const reader = new FileReader()
      reader.onload = (e) => {
        this.imageUrl = e.target?.result;
      };
      reader.readAsDataURL(this.selectedFile);
    }

    console.log(this.selectedFile?.name);
  }

  removeAfterSpace(input: string): string {
    const indexOfSpace = input.indexOf(' ');

    if (indexOfSpace !== -1) {
      return input.substring(0, indexOfSpace);
    }

    return input;
  }

  onSubmit(){
    this.submitted = true;
    this.wrongEmail = false;
    const contactNumber = `${this.partnerForm.value.contactNumber}`;
    const telephoneNumber = `${this.partnerForm.value.telephoneNumber}`;

    if (this.partnerForm.invalid) {
      console.log('wrong form')
      return;
    }

    Object.keys(this.partnerForm.controls).forEach(key => {
      if(key != "photo")
      {
        const control = this.partnerForm.get(key);
        if (control) {
          control.setValue(control.value);
        }
      }
    });
    // console.log(JSON.stringify(this.partnerForm.value, null, 2));

    console.log(JSON.stringify(this.partnerForm.value, null, 2));



    const combinedContactNumber = `${ this.removeAfterSpace(this.partnerForm.value.conpref)} ${this.partnerForm.value.contactNumber}`;
    const combinedTelephoneNumber = `${this.removeAfterSpace(this.partnerForm.value.numpref)} ${this.partnerForm.value.telephoneNumber}`;

    this.partnerForm.patchValue({
      contactNumber: combinedContactNumber,
      telephoneNumber: combinedTelephoneNumber,
    });

    console.log(JSON.stringify(this.partnerForm.value, null, 2));
    console.log(this.partnerForm)

    //console.log(JSON.stringify(this.partnerForm.value, null, 2));
    //console.log(this.partnerForm)

    this.authService.registerPartner(this.partnerForm.value as RegisterPartnerDto, this.selectedFile).subscribe(
      (response: RegisterResponseDto) => {
        console.log('response:', response);

        if (response.message == 'success') {
          // const combinedContactNumber = `${ this.removeAfterSpace(this.partnerForm.value.conpref)} ${this.partnerForm.value.contactNumber}`;
          // const combinedTelephoneNumber = `${this.removeAfterSpace(this.partnerForm.value.numpref)} ${this.partnerForm.value.telephoneNumber}`;

          // this.partnerForm.patchValue({
          //   contactNumber: combinedContactNumber,
          //   telephoneNumber: combinedTelephoneNumber,
          // });
          this.toastService.showSuccess('Account created, now log in');
          this.router.navigate(['']);
        }
        else {
          if(response.message == 'email not available')
          {
            this.wrongEmail = true;
            this.partnerForm.patchValue({
              contactNumber: contactNumber,
              telephoneNumber: telephoneNumber,
            });
          }
          else if(response.message == 'photo do not have a suitable extension')
          {
            this.wrongPhoto = true;
            console.log('wrong photo');
          }
        }
        // console.log('wrong email?: ', this.wrongEmail)
      },
      (error) => {
        console.error('Error while registering a user:', error);
      }
    );
  }

  get f(): { [key: string]: AbstractControl } {
    return this.partnerForm.controls;
  }

  get addressForm(): FormGroup {
    return this.partnerForm.get('address') as FormGroup;
  }

  formatPostalCode(event: any): void {

    const cleanedValue = event.target.value.replace(/-/g, '');

    if (/^\d+$/.test(cleanedValue)) {

      const formattedValue = cleanedValue.slice(0, 2) + '-' + cleanedValue.slice(2);
      this.addressForm.patchValue({ postalCode: formattedValue });

    } else {
      const newValue = cleanedValue.slice(0, -1);
      this.addressForm.patchValue({ postalCode: newValue });
    }
  }
}
