import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './developer.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DeveloperDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const developerEntity = useAppSelector(state => state.developer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="developerDetailsHeading">
          <Translate contentKey="norApp.developer.detail.title">Developer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{developerEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="norApp.developer.name">Name</Translate>
            </span>
          </dt>
          <dd>{developerEntity.name}</dd>
          <dt>
            <span id="age">
              <Translate contentKey="norApp.developer.age">Age</Translate>
            </span>
          </dt>
          <dd>{developerEntity.age}</dd>
          <dt>
            <Translate contentKey="norApp.developer.skill">Skill</Translate>
          </dt>
          <dd>
            {developerEntity.skills
              ? developerEntity.skills.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.name}</a>
                    {developerEntity.skills && i === developerEntity.skills.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/developer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/developer/${developerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DeveloperDetail;
