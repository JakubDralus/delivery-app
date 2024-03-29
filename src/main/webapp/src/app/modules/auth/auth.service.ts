import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthRequestDto, AuthResponseDto, RegisterPartnerDto, RegisterResponseDto, RegisterUserDto, RegisterDeliveryManDto } from 'src/app/shared/model/api-models';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  loggedUser = {
    firstName: '',
    lastName: ''
  }

  apiUrl: string = 'http://localhost:8080';

  constructor(
    private http: HttpClient,
  ) { }


  registerUser(user: RegisterUserDto): Observable<RegisterResponseDto> {
    return this.http.post(`${this.apiUrl}/api/auth/register/user`, user);
  }

  registerPartner(partner: RegisterPartnerDto, photo: File | null): Observable<RegisterResponseDto> {
    const formData:FormData = new FormData();
    if(photo != null)
    {
      formData.append('photo', photo);
    }
    formData.append('partner', new Blob([JSON.stringify(partner)], {type: "application/json"}));
    return this.http.post(`${this.apiUrl}/api/auth/register/partner`, formData);
  }

  registerDeliveryMan(deliveryMan: RegisterDeliveryManDto): Observable<RegisterResponseDto> {
    return this.http.post(`${this.apiUrl}/api/auth/register/courier`, deliveryMan);
  }

  logIn(loginObj: AuthRequestDto): Observable<AuthResponseDto> {
    return this.http.post(`${this.apiUrl}/api/auth/authenticate`, loginObj);
  }

  setLoggedUser(responseObj: AuthResponseDto): void {
    // localStorage.clear();
    localStorage.setItem('id', responseObj.id?.toString() || '');
    localStorage.setItem('authToken', responseObj.token || '');
    localStorage.setItem('role', responseObj.role || '');
    localStorage.setItem('expirationDate', responseObj.expirationDate || '');
    localStorage.setItem('firstName', responseObj.firstName || '');
    localStorage.setItem('lastName', responseObj.lastName || '');

    console.log(responseObj);
  }

  setCredentials(fn: any, ln: any): void {
    localStorage.setItem('firstName', fn);
    localStorage.setItem('lastName', ln);
  }

  isUserLogged(): boolean {
    return localStorage.getItem('authToken') ? true : false;
  }

  getLoggedUser() {
    const firstName = localStorage.getItem('firstName') || '';
    const lastName = localStorage.getItem('lastName') || '';

    this.loggedUser.firstName = firstName;
    this.loggedUser.lastName = lastName;
    return this.loggedUser;
  }

  getDecodedAccessToken(token: string): any {
    try {
      return jwtDecode(token);
    } catch(Error) {
      return null;
    }
  }
  getLoggedUserEmail() {
    const token = localStorage.getItem('authToken') || '';

    const tokenInfo = this.getDecodedAccessToken(token); // decode token
    const email = tokenInfo.sub; // get token expiration dateTime
    console.log("email::"+email);
    return email;
  }
  isTokenExpired(): boolean {
    const expirationDateStr = localStorage.getItem('expirationDate');

    if (expirationDateStr) {
      const expirationDate = new Date(expirationDateStr);
      return expirationDate <= new Date(); //less than
    }
    return true; // Token expiration information not found
  }


  logOut(): void {
    localStorage.clear();
  }
}
