import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng-lts/api';

import { IEtablissement } from '../etablissement.model';
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';
import { DataUtils } from 'app/core/util/data-util.service';
import { PrimeNGConfig } from 'primeng-lts/api';

// import { GoogleMap } from '@agm/core/services/google-maps-types';


@Component({
  selector: 'jhi-etablissement-detail',
  templateUrl: './etablissement-detail.component.html',
  styleUrls: ['./etablissement-detail.component.scss'],
})
export class EtablissementDetailComponent implements OnInit {
  etablissement: IEtablissement | null = null;

    options: any;

    overlays: any[] = [];

    dialogVisible!: boolean;

    markerTitle?: string | null;

    selectedPosition: any;

    infoWindow: any;

    draggable!: boolean;

  constructor(protected activatedRoute: ActivatedRoute, private primengConfig: PrimeNGConfig, protected dataUtils: DataUtils, private messageService: MessageService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etablissement }) => {
      this.etablissement = etablissement;
    });
    this.options = {
      center: {lat: 36.890257, lng: 30.707417},
      zoom: 12
    };
    this.overlays = [
      new google.maps.Marker({position: {lat: 36.879466, lng: 30.667648}, title:"Konyaalti"}),
      new google.maps.Marker({position: {lat: 36.883707, lng: 30.689216}, title:"Ataturk Park"}),
      new google.maps.Marker({position: {lat: 36.885233, lng: 30.702323}, title:"Oldtown"}),
      new google.maps.Polygon({paths: [
          {lat: 36.9177, lng: 30.7854},{lat: 36.8851, lng: 30.7802},{lat: 36.8829, lng: 30.8111},{lat: 36.9177, lng: 30.8159}
      ], strokeOpacity: 0.5, strokeWeight: 1,fillColor: '#1976D2', fillOpacity: 0.35
      }),
      new google.maps.Circle({center: {lat: 36.90707, lng: 30.56533}, fillColor: '#1976D2', fillOpacity: 0.35, strokeWeight: 1, radius: 1500}),
      new google.maps.Polyline({path: [{lat: 36.86149, lng: 30.63743},{lat: 36.86341, lng: 30.72463}], geodesic: true, strokeColor: '#FF0000', strokeOpacity: 0.5, strokeWeight: 2})
  ];
  }

  download(): void { 
     // const element = document.getElementById('identification')!;

      // html2canvas(element).then((canvas) => {

      //   const imgData = canvas.toDataURL('image/png');

      //   const doc = new jsPDF();
        
      //   const imgHeight = canvas.height * 208 / canvas.width;
        
      //   doc.addImage(imgData, 0, 0, 208, imgHeight);

      //   doc.save('image.pdf');
      // })

      const doc = new jsPDF();

      html2canvas(document.querySelector("#content") as HTMLImageElement).then(canvas => {
        const imgData = canvas.toDataURL('image/png');
        
        const imgHeight = canvas.height * 208 / canvas.width;
        
        doc.addImage(imgData, 0, 0, 210, imgHeight);

        doc.save("etablissement.pdf");
        
      });
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }
  
  previousState(): void {
    window.history.back();
  }
  
}
