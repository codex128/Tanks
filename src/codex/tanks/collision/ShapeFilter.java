/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.collision;

import codex.tanks.components.CollisionShape;
import codex.tanks.components.GameObject;
import codex.tanks.components.Team;
import codex.tanks.es.EntityAccess;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public interface ShapeFilter {
    
    public boolean filter(EntityAccess access, CollisionShape shape);
    
    public static ShapeFilter byGameObject(String type) {
        return new GameObjectFilter(type);
    }
    public static ShapeFilter byGroup(int group) {
        return new GroupFilter(group);
    }
    public static ShapeFilter byTeam(int team) {
        return new TeamFilter(team);
    }
    public static ShapeFilter byId(EntityId id) {
        return new IdFilter(id);
    }
    public static ShapeFilter and(ShapeFilter... filters) {
        return new AndFilter(filters);
    }
    public static ShapeFilter or(ShapeFilter... filters) {
        return new OrFilter(filters);
    }
    public static ShapeFilter none(ShapeFilter... filter) {
        return new NotFilter(filter);
    }
    public static ShapeFilter notId(EntityId id) {
        return none(byId(id));
    }
    public static ShapeFilter nullProtection(ShapeFilter filter) {
        return new NullProtector(filter);
    }
    
    public static class NullProtector implements ShapeFilter {
        private final ShapeFilter filter;        
        public NullProtector(ShapeFilter filter) {
            this.filter = filter;
        }        
        @Override
        public boolean filter(EntityAccess access, CollisionShape shape) {
            return filter == null || filter.filter(access, shape);
        }        
    }
    public static class GameObjectFilter implements ShapeFilter {
        private final String type;
        private GameObjectFilter(String type) {
            this.type = type;
        }
        @Override
        public boolean filter(EntityAccess access, CollisionShape shape) {
            var object = access.ed.getComponent(access.id, GameObject.class);
            return object != null && object.getType().equals(type);
        }
    }
    public static class GroupFilter implements ShapeFilter {
        private final int group;
        private GroupFilter(int group) {
            this.group = group;
        }
        @Override
        public boolean filter(EntityAccess access, CollisionShape shape) {
            return shape.getGroup() == group;
        }
    }
    public static class TeamFilter implements ShapeFilter {
        private final int team;
        private TeamFilter(int team) {
            this.team = team;
        }
        @Override
        public boolean filter(EntityAccess access, CollisionShape shape) {
            var t = access.ed.getComponent(access.id, Team.class);
            return t != null && t.getTeam() == team;
        }
    }
    public static class IdFilter implements ShapeFilter {
        private final EntityId id;
        private IdFilter(EntityId id) {
            this.id = id;
        }
        @Override
        public boolean filter(EntityAccess access, CollisionShape shape) {
            return id == null || id.equals(access.id);
        }        
    }
    public static class AndFilter implements ShapeFilter {
        private final ShapeFilter[] filters;
        private AndFilter(ShapeFilter... filters) {
            this.filters = filters;
        }
        @Override
        public boolean filter(EntityAccess access, CollisionShape shape) {
            for (var f : filters) {
                if (!f.filter(access, shape)) return false;
            }
            return true;
        }        
    }
    public static class OrFilter implements ShapeFilter {
        private final ShapeFilter[] filters;
        private OrFilter(ShapeFilter... filters) {
            this.filters = filters;
        }
        @Override
        public boolean filter(EntityAccess access, CollisionShape shape) {
            for (var f : filters) {
                if (f.filter(access, shape)) return true;
            }
            return false;
        }        
    }
    public static class NotFilter implements ShapeFilter {
        private final ShapeFilter[] filters;
        private NotFilter(ShapeFilter... filters) {
            this.filters = filters;
        }
        @Override
        public boolean filter(EntityAccess access, CollisionShape shape) {
            for (var f : filters) {
                if (f.filter(access, shape)) return false;
            }
            return true;
        }
    }
    
}
