import { Component, OnInit } from '@angular/core';
import { ForumCreate, ForumMessagesCreate } from 'src/app/models/forum/forum-create/forum-create';
import { ForumDto, ForumMessagesDto } from 'src/app/models/forum/forum-dto/forum-dto';
import { ForumRestClientService } from 'src/app/services/api/forum/forum-rest-client.service';
import { NotificationsService } from 'src/app/services/notifications/notifications.service';

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
    private notificationService: NotificationsService
  ){
    this.forum = new ForumDto();
    this.newMessage = '';
  }

  ngOnInit(): void {
      this.getAllMessages();
  }

  private getAllMessages(): void {
    this.forumService.getForumByMapsId(localStorage.getItem('access_token')!, localStorage.getItem('actual_maps_id')!).subscribe(
      (data: ForumDto) => {
        this.forum = data;
      },
      (err: Error) => {
        this.notificationService.showError(err.message);
      }
    )
  }

  public sendNewMessage(): void {
    let newMessage = new ForumMessagesCreate();
    newMessage.message = this.newMessage;
    newMessage.username = localStorage.getItem('actual_username')!;
    let forumCreate = new ForumCreate();
    forumCreate.mapsId = this.forum.mapsId;
    forumCreate.listMessages = [newMessage];
    this.forumService.updateForum(localStorage.getItem('access_token')!, this.forum.id, forumCreate).subscribe(
      (data: ForumDto) => {
        this.forum = data;
      },
      (err: Error) => {
        this.notificationService.showError(err.message);
      }
    )
  }

  public deleteForumMessage(idMessage: string): void {
    let list: ForumMessagesDto[] = this.forum.listMessages.filter(message => message.id != idMessage);
    let listMapper: ForumMessagesCreate[] = [];

    list.forEach((message : ForumMessagesDto) => {
      let mapper = new ForumMessagesCreate();
      mapper.message = message.message;
      mapper.username = message.username;
      listMapper.push(mapper);
    });


    let forumUpdate = new ForumCreate();
    forumUpdate.mapsId = this.forum.mapsId;
    forumUpdate.listMessages = listMapper;

    this.forumService.updateForum(localStorage.getItem('access_token')!, this.forum.id, forumUpdate).subscribe(
      (data: ForumDto) => {
        this.notificationService.showCorrect('Mensaje eliminado correctamente');
      },
      (err: Error) => {
        this.notificationService.showError(err.message);
      }
    )
  }



}
