import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { environment } from 'src/main/webapp/environment';
// import { IRole } from '../../role/role.model';
// import { any } from '../user.model';
// import { environment } from 'src/environments/environment';
// import { any } from '../userK.model';
export type EntityResponseType = HttpResponse<any>;
export type EntityResponseTypeK = HttpResponse<any>;

@Injectable({
  providedIn: 'root'
})
export class UserService {

  // Define API
  apiURL = `${environment.keycloakUrl}${environment.baserealm}${environment.realm}/`;
  apiPortailRH=`${environment.portailRHurl}`

  constructor(private http: HttpClient) { }

  /*========================================
    CRUD Methods for consuming RESTful API
  =========================================*/

  // Http Options
  // httpOptions = {
  //   headers: new HttpHeaders({
  //     'Content-Type': 'application/json',
  //   //  'Access-Control-Allow-Origin':'*'
  //   })
  // }  

  // HttpClient API get() method => Fetch agents list
  // getUser(): Observable<EntityResponseType> {
  //   return this.http.get<any>(this.apiPortailRH+'api/userK/users',{
  //     headers:this.httpOptions.headers,
  //     observe:'response'
  //     })
  //   .pipe(
  //     retry(1),
  //     //catchError(this.handleError)
  //   )
  // }


// @@@@@@@@@@@@@@@@@@@@@@@@@@@ get user by username   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

  // getUserByUsername(Myusername:string): Observable<HttpResponse<any[]>> {
  //   return this.http.get<any>(this.apiPortailRH+"api/userK/users/"+Myusername,{
  //     headers:this.httpOptions.headers,
  //     observe:'response'
  //     })
  //   .pipe(
  //     retry(1),
  //    // catchError(this.handleError)
  //   )
  // }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@  get all roles of a realm @@@@@@@@@@@@@@@@@@@@@@@@@@@@

  // getRoles(): Observable<HttpResponse<IRole[]>> {
  //   return this.http.get<IRole[]>(this.apiURL + 'roles',{
  //     headers:this.httpOptions.headers,
  //     observe:'response'
  //     })
  //   .pipe(
  //     retry(1),
  //  //   catchError(this.handleError)
  //   )
  // }
  //  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ user create @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // create(user: any): Observable<EntityResponseType> {
   
  //   return this.http.post<any>(this.apiURL+'users', user, { 
  //   headers:this.httpOptions.headers,
  //     observe: 'response' });
    
  // }

   //  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ userK create @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  //  createUserk(userK: any): Observable<EntityResponseTypeK> {
   
  //   return this.http.post<any>(this.apiPortailRH+'api/userK/users', userK, { 
  //   headers:this.httpOptions.headers,
  //     observe: 'response' });
    
  // }
  //  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ update user @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // update(user: any): Observable<EntityResponseType> {
  //   return this.http.put<any>(this.apiPortailRH+'api/userK/users', user, { 
  //     headers:this.httpOptions.headers,
  //     observe: 'response' });
    
  //  }
  
  // //Delete a use
  // delete(user:any){
  //   return this.http.delete(this.apiPortailRH+'api/userK/users/'+user.id,{
  //     headers:this.httpOptions.headers,
  //     observe: 'response'
  //   })
  // }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ realm role-mapping to a user @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

//   roleMapping(role: IRole[], userID:string): Observable<HttpResponse<IRole[]>> {
   
//     return this.http.post<IRole[]>(this.apiURL+'users/'+userID+'/role-mappings/realm', role, { 
//       //auth/admin/realms/jhipster/clients/898488c8-e260-41c5-a463-7ceea14d587a/roles
//       ///auth/admin/realms/{realm}/users/{user}/role-mappings/clients/{client}
//       headers:this.httpOptions.headers,
//       observe: 'response' });

//  }

//  getRoles(): Observable<HttpResponse<IRole[]>> {
//     return this.http.get<IRole[]>(this.apiURL + 'roles',{
//       headers:this.httpOptions.headers,
//       observe:'response'
//       })
//     .pipe(
//       retry(1),
//    //   catchError(this.handleError)
//     )
//   }

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ role-mapping to a user @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

// clientRoleMapping(role: IRole[], userID:string): Observable<HttpResponse<IRole[]>> {
   
//   return this.http.post<IRole[]>(this.apiURL+'users/'+userID+'/role-mappings/clients/898488c8-e260-41c5-a463-7ceea14d587a', role, { 
//     //auth/admin/realms/jhipster/clients/898488c8-e260-41c5-a463-7ceea14d587a/roles
//     ///auth/admin/realms/{realm}/users/{user}/role-mappings/clients/{client}
//     headers:this.httpOptions.headers,
//     observe: 'response' });

// }



//  assignedProfilUser(userID: any,groupId:any){
// return this.http.put<any>(this.apiURL+'users/'+userID+'/groups/'+groupId,{ 
//   headers:this.httpOptions.headers,
//   observe: 'response' });

// }

// gettoUser(){
// return this.http.get<any>(this.apiURL+'users',{
//   //headers:this.httpOptions.headers,
//   observe: 'response' 
// })
// }
 

//  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ error handler @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

  /* handleError(handleError: any): import("rxjs").OperatorFunction<HttpResponse<any>, any> {
    throw new Error('Method not implemented.');
  } */


  // getEvent(): Observable<HttpResponse<any[]>> {
  //   return this.http.get<any>('http://localhost:9080/auth/admin/realms/jhipster/admin-events',{
  //     headers:this.httpOptions.headers,
  //     observe:'response'
  //     })
  //   .pipe(
  //     retry(1),
  //    // catchError(this.handleError)
  //   )
  // }
  // desactiver(userId:any,groupId:any){
  //   return this.http.delete<any>(this.apiURL+'users/'+userId+'/groups/'+groupId,{ 
  //     headers:this.httpOptions.headers,
  //     observe: 'response' });
    
  //   }
  
}