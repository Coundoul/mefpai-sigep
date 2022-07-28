import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng-lts/api';

import { IEtablissement } from '../etablissement.model';

declare let google: any;

@Component({
  selector: 'jhi-etablissement-detail',
  templateUrl: './etablissement-detail.component.html',
  styleUrls: ['./etablissement-detail.component.scss'],
})
export class EtablissementDetailComponent implements OnInit {
  etablissement: IEtablissement | null = null;

    options: any;

    overlays: any[] = [];

    dialogVisible = false;

    markerTitle?: string | null;

    selectedPosition: any;

    infoWindow: any;

    draggable = false;

  constructor(protected activatedRoute: ActivatedRoute, private messageService: MessageService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etablissement }) => {
      this.etablissement = etablissement;
    });

  //   this.options = {
  //     center: {lat: 36.890257, lng: 30.707417},
  //     zoom: 12
  // };

  //   this.initOverlays();

  //   this.infoWindow = new google.maps.InfoWindow();
  }

  previousState(): void {
    window.history.back();
  }

//   handleMapClick(event: any): void {
//     this.dialogVisible = true;
//     this.selectedPosition = event.latLng;
// }

// handleOverlayClick(event: any): void {
//     const isMarker = event.overlay.getTitle ;

//     if (isMarker) {
//         const title = event.overlay.getTitle();
//         this.infoWindow.setContent(` MBR${String(title)} `);
//         this.infoWindow.open(event.map, event.overlay);
//         event.map.setCenter(event.overlay.getPosition());

//         this.messageService.add({severity:'info', summary:'Marker Selected', detail: title});
//     }
//     else {
//         this.messageService.add({severity:'info', summary:'Shape Selected', detail: ''});
//     }
// }

// addMarker(): void{
//     this.overlays.push(new google.maps.Marker({position:{lat: this.selectedPosition.lat(), lng: this.selectedPosition.lng()}, title:this.markerTitle, draggable: this.draggable}));
//     this.markerTitle = null;
//     this.dialogVisible = false;
// }

// handleDragEnd(event: any): void{
//     this.messageService.add({severity:'info', summary:'Marker Dragged', detail: event.overlay.getTitle()});
// }

// // initOverlays(): void{
// //     if (!this.overlays||!this.overlays.length) {
// //         this.overlays = [
// //             new google.maps.Marker({position: {lat: 36.879466, lng: 30.667648}, title:"Konyaalti"}),
// //             new google.maps.Marker({position: {lat: 36.883707, lng: 30.689216}, title:"Ataturk Park"}),
// //             new google.maps.Marker({position: {lat: 36.885233, lng: 30.702323}, title:"Oldtown"}),
// //             new google.maps.Polygon({paths: [
// //                 {lat: 36.9177, lng: 30.7854},{lat: 36.8851, lng: 30.7802},{lat: 36.8829, lng: 30.8111},{lat: 36.9177, lng: 30.8159}
// //             ], strokeOpacity: 0.5, strokeWeight: 1,fillColor: '#1976D2', fillOpacity: 0.35
// //             }),
// //             new google.maps.Circle({center: {lat: 36.90707, lng: 30.56533}, fillColor: '#1976D2', fillOpacity: 0.35, strokeWeight: 1, radius: 1500}),
// //             new google.maps.Polyline({path: [{lat: 36.86149, lng: 30.63743},{lat: 36.86341, lng: 30.72463}], geodesic: true, strokeColor: '#FF0000', strokeOpacity: 0.5, strokeWeight: 2})
// //         ];
// //     }
// // }

// // zoomIn(map: any): void{
// //     map.setZoom(map.getZoom() + 1);
// // }

// // zoomOut(map: any): void{
// //     map.setZoom(map.getZoom() - 1);
// // }

// clear(): void{
//     this.overlays = [];
// }

  
}
