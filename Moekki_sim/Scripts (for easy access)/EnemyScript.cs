using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using BreadcrumbAi;
using UnityEngine.UI;

public class EnemyScript : MonoBehaviour
{
    Ai ai;

    public Slider hpSlider;

    public GameObject corpse;
    bool dead = false;
    //public GameObject player;
    public Rigidbody rb;
    public Animator animator;
    public float health;
    float maxHealth;
    public float damage;

    public Transform attackPoint;
    public float attackRange = 0.5f;

    public LayerMask playerLayers;
    public float despawnTime;
    /*private bool isDead = false;*/
    public Vector3 oldPosition;

    bool isAttacking = false;

    public float damageInterval;


    
    // Start is called before the first frame update
    void Start()
    {
        maxHealth = health;
        ai = GetComponent<Ai>();
        setRigidbodyState(true);
        //setColliderState(false);
        hpSlider.gameObject.SetActive(false);
        

        
    }

    // Update is called once per frame
    void Update()
    {
        /*Vector3 direction = transform.position - oldPosition;
        float forwardTest = Vector3.Dot(-direction.normalized, transform.position.normalized);*/
        if(health < maxHealth && dead == false){
            hpSlider.gameObject.SetActive(true);
        }

        if(ai.moveState == Ai.MOVEMENT_STATE.IsFollowingPlayer){
            animator.SetBool("Moving", true);
        }
        /*else if(forwardTest>0.01f){
            animator.SetFloat("forward", 1f);
        }*/
        else{
            animator.SetBool("Moving", false);
        }

        

        
        

        //If the enemy can hit the player (BreadcrumbAI)
        if(ai.attackState == Ai.ATTACK_STATE.CanAttackPlayer && dead == false){
            if(!isAttacking){
                StartCoroutine(DealDamage());
            }
            
        }
        
   
        oldPosition = transform.position;

        
    }

    void setRigidbodyState(bool state){
        Rigidbody[] rigidbodies = GetComponentsInChildren<Rigidbody>();

        foreach(Rigidbody rigidbody in rigidbodies){
            rigidbody.isKinematic = state;
        }

        GetComponent<Rigidbody>().isKinematic = !state;
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    void explode(){
        Rigidbody[] rigidbodies = GetComponentsInChildren<Rigidbody>();
        
        foreach(Rigidbody rigidbody in rigidbodies){
            rigidbody.AddExplosionForce(12f, rigidbody.position, 5f, 0f);
        }
    }
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    //RAGDOLLING
    void setColliderState(bool state){
        
        GetComponent<Collider>().enabled = state;
    }
    IEnumerator DealDamage()
    {
        isAttacking = true;

        while(ai.attackState == Ai.ATTACK_STATE.CanAttackPlayer){
            animator.SetBool("attack", true);

            Collider[] hitPlayers = Physics.OverlapSphere(attackPoint.position, attackRange, playerLayers);
            foreach (Collider player in hitPlayers){

            Debug.Log("Hit player!");
            player.GetComponent<PlayerBattleScript>().TakeDamage(damage);
            yield return new WaitForSeconds(damageInterval); 
            
            }
        }
        
        animator.SetBool("attack", false);
        
        //Play attack animation
        isAttacking = false;
        yield return null;
        
        
        
    }

    void OnDrawGizmosSelected(){
        Gizmos.DrawWireSphere(attackPoint.position, attackRange);
    }
    
    public void die(){

        dead = true;
        hpSlider.gameObject.SetActive(false);
        GetComponent<Animator>().enabled = false;
        setRigidbodyState(false);
        setColliderState(false);
        gameObject.transform.GetChild(0).gameObject.SetActive(false);
        gameObject.transform.GetChild(1).gameObject.SetActive(false);
        //SetActive(false);
        ai.lifeState = Ai.LIFE_STATE.IsDead;
        Instantiate(corpse, gameObject.transform);
        
        explode();
        


        Destroy(gameObject, despawnTime);
        
    }
    

    public void TakeDamage(float damage){
        health -= damage;

        if(health <= 0){
            die();
        }

        hpSlider.value -=damage;
    }
}
