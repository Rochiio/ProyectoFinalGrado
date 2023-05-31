import { Component, OnInit } from '@angular/core';
import { ForumCreate, ForumMessagesCreate } from 'src/app/models/forum/forum-create/forum-create';
import { ForumDto, ForumMessagesDto } from 'src/app/models/forum/forum-dto/forum-dto';
import { ForumRestClientService } from 'src/app/services/api/forum/forum-rest-client.service';
import { NotificationsService } from 'src/app/services/notifications/notifications.service';
import { DateFormatterService } from 'src/app/utils/date-formatter/date-formatter.service';
import { MappersService } from 'src/app/mappers/mappers.service';

@Component({
  selector: 'app-forum-principal',
  templateUrl: './forum-principal.component.html',
  styleUrls: ['./forum-principal.component.css']
})
export class ForumPrincipalComponent implements OnInit{
  public forum: ForumDto;
  public newMessage: string;

  constructor(
    private forumService: ForumRestClientService,
    private notificationService: NotificationsService,
    private mapperService: MappersService,
    private formatterService: DateFormatterService
  ){
    this.forum = new ForumDto();
    this.newMessage = '';
  }

  ngOnInit(): void {
      this.getAllMessages();
  }


  /**
   * Coonseguir el foro.
   */
  private getAllMessages(): void {
    this.forumService.getForumByMapsId(localStorage.getItem('access_token')!, localStorage.getItem('actual_maps_id')!).subscribe(
      (data: ForumDto) => {
        this.forum = data;
      },
      (err: ErrorEvent) => {
        this.notificationService.showError(err.error);
      }
    )
  }

  /**
   * Enviar un nuevo mensaje al foro.
   */
  public sendNewMessage(): void {
    let newMessage = new ForumMessagesDto();
    newMessage.message = this.newMessage;
    newMessage.username = localStorage.getItem('actual_username')!;
    newMessage.created_At = this.formatterService.transformDate(new Date());

    let newList: ForumMessagesDto[] = this.forum.listMessages
    newList.push(newMessage);

    let forumCreate = new ForumCreate();
    forumCreate.mapsId = this.forum.mapsId;
    forumCreate.listMessages = this.mapperService.forumMessagesDtoToCreate(newList);

    this.forumService.updateForum(localStorage.getItem('access_token')!, this.forum.id, forumCreate).subscribe(
      (data: ForumDto) => {
        this.forum = data;
        this.notificationService.showCorrect('Mensaje creado correctamente');
      },
      (err: ErrorEvent) => {
        this.notificationService.showError(err.error);
      }
    )
  }

  /**
   * Eliminar un mensaje del foro.
   * @param idMessage id del mensaje a eliminar.
   */
  public deleteForumMessage(idMessage: string): void {
    let list: ForumMessagesDto[] = this.forum.listMessages.filter(message => message.id != idMessage);

    let listMapper: ForumMessagesCreate[] = this.mapperService.forumMessagesDtoToCreate(list);

    let forumUpdate = new ForumCreate();
    forumUpdate.mapsId = this.forum.mapsId;
    forumUpdate.listMessages = listMapper;

    this.forumService.updateForum(localStorage.getItem('access_token')!, this.forum.id, forumUpdate).subscribe(
      (data: ForumDto) => {
        this.forum.listMessages = data.listMessages;
        this.notificationService.showCorrect('Mensaje eliminado correctamente');
      },
      (err: ErrorEvent) => {
        this.notificationService.showError(err.error);
      }
    )
  }





}
