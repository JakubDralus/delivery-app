import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OrderDto, OrderReadDto, ProductOrderDto } from 'src/app/shared/model/api-models';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  apiUrl: string = "http://localhost:8080";
  
  constructor(private http: HttpClient) {}

  getOrders(): Observable<OrderDto[]> {
    return this.http.get<OrderDto[]>(`${this.apiUrl}/api/orders`);
  }
  getOrdersAssignedToCourier(email: string): Observable<OrderReadDto[]> {
    return this.http.get<OrderReadDto[]>(`${this.apiUrl}/api/orders/assigned/`+email);
  }
  getOrdersAssignedToUser(id: number): Observable<OrderReadDto[]> {
    return this.http.get<OrderReadDto[]>(`${this.apiUrl}/api/orders/my-orders/`+id);
  }
  setRating(id?: number, rating?: number): Observable<OrderReadDto> {
    // console.log(rating+"TAK"+id);
    // console.log(`${this.apiUrl}/api/orders/rating/`+id);
    const s = `${this.apiUrl}/api/orders/rating/`+id;
    console.log(s);
    return this.http.put<OrderReadDto>(s, rating);
  }
  setStatus(order: OrderReadDto): Observable<OrderReadDto> {
    return this.http.put<OrderReadDto>(`${this.apiUrl}/api/orders/make-done/${order.id}`, order.status);
  }
  updateOrder(order: OrderDto): Observable<OrderDto> {
    return this.http.put<OrderDto>(`${this.apiUrl}/api/orders/${order.id}`, order);
  }

  deleteOrder(orderId: number): Observable<OrderDto> {
    return this.http.delete<OrderDto>(`${this.apiUrl}/api/orders/${orderId}`);
  }

  makeOrder(order: OrderDto): Observable<OrderDto> {
    return this.http.post<OrderDto>(`${this.apiUrl}/api/orders`, order);
  }

  makeProductOrder(productOrder: ProductOrderDto) {
    return this.http.post<OrderDto>(`${this.apiUrl}/api/product_order`, productOrder);
  }
}