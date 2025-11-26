import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminContenidoListComponent } from './admin-contenido-list.component';

describe('AdminContenidoListComponent', () => {
  let component: AdminContenidoListComponent;
  let fixture: ComponentFixture<AdminContenidoListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminContenidoListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminContenidoListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
