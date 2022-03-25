import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SeniorityLevel from './seniority-level';
import Skill from './skill';
import Developer from './developer';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}seniority-level`} component={SeniorityLevel} />
      <ErrorBoundaryRoute path={`${match.url}skill`} component={Skill} />
      <ErrorBoundaryRoute path={`${match.url}developer`} component={Developer} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
