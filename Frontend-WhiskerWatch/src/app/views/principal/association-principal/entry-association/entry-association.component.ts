import { Component, Input, OnInit } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { AssociationDto } from 'src/app/models/association/association-dto/association-dto';
import { AssociationRestClientService } from 'src/app/services/api/association/association-rest-client.service';

@Component({
  selector: 'app-entry-association',
  templateUrl: './entry-association.component.html',
  styleUrls: ['./entry-association.component.css']
})
export class EntryAssociationComponent implements OnInit{
  @Input()
  public entry!: AssociationDto;
  public image!: SafeUrl;
  public isImage: boolean = false;

  constructor(
    private associationService: AssociationRestClientService,
    private sanitizer: DomSanitizer
  ){}

  ngOnInit(): void {
    if(this.entry.image != null){
      this.associationService.getAssociationImage(localStorage.getItem('access_token')!, this.entry.id).subscribe(
        (data: Blob) => {
          let unsafeImageUrl = URL.createObjectURL(data);
          let imageUrl = this.sanitizer.bypassSecurityTrustUrl(unsafeImageUrl);
          this.image = imageUrl;
          this.isImage = true;
        },
        (err: Error) => {
          console.log('Error para obtener la imagen');
        }
      );
    }
  }


}
