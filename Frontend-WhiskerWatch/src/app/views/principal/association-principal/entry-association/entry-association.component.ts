import { Component, Input } from '@angular/core';
import { AssociationDto } from 'src/app/models/association/association-dto/association-dto';

@Component({
  selector: 'app-entry-association',
  templateUrl: './entry-association.component.html',
  styleUrls: ['./entry-association.component.css']
})
export class EntryAssociationComponent {
  @Input()
  public entry!: AssociationDto

  //TODO: Añadir llamada API get images.
  constructor(){}

}
