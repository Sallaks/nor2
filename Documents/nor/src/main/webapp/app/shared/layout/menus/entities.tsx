import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/seniority-level">
      <Translate contentKey="global.menu.entities.seniorityLevel" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/skill">
      <Translate contentKey="global.menu.entities.skill" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/developer">
      <Translate contentKey="global.menu.entities.developer" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
